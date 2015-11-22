import psycopg2
import psycopg2.extras
import itertools
import json
import click


multilang_fields = ['description', 'tags', 'attributes', 'raw']


def map_doc(item):
    lang = item['lang']
    description = item.get('full_description', item.get('short_description'))
    attributes = item.get('attributes')
    address = item.get('address')
    tags = item.get('tags')
    doc = {
        'source_id': item['id'],
        'name': item.get('name'),
        'headline': item.get('headline'),
        'logo': item.get('logo'),
        'description': {lang: description} if description else None,
        'rubrics': item.get('categories'),
        'attributes': {lang: attributes} if attributes else None,
        'address': {'full_address': address} if address else None,
        'contacts': item.get('contacts'),
        'photos': item.get('photos'),
        'geometry': item.get('geometry'),
        'tags': {lang: tags} if tags else None,
        'raw': {lang: item}
    }
    return dict((k, v) for k, v in doc.iteritems() if v)


def merge_items(docs):
    """ Merge document versions on different languages to one multilingual doc

    @param docs: iterable
    """
    res = None
    for doc in docs:
        if not res:
            res = doc
        else:
            for k, v in doc.iteritems():
                if k in multilang_fields:
                    res_val = res.get(k, {})
                    res_val.update(v)
                    res[k] = res_val
                elif k == 'photo':
                    res['photos'] = set(res.get('photos', []) + v)
                else:
                    if not res.get(k):
                        res[k] = v
    return res


@click.command()
@click.argument('f', type=click.Path(exists=True))
@click.option('--source', type=click.STRING, help='Source type, example: osm')
@click.option('--postgres-dsn', help='Connection dsn to postgres')
def import_data(f, source, postgres_dsn):
    with open(f) as fp:
        data = [json.loads(line) for line in fp.readlines()]
    data = sorted(data, key=lambda r: r['id'])

    docs = []
    doc_ids = set()
    for item_id, items in itertools.groupby(data, key=lambda r: r['id']):
        doc = merge_items(map(map_doc, items))
        docs.append(doc)
        doc_ids.add(str(item_id))

    conn = psycopg2.connect(postgres_dsn)
    cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
    sql = 'select * from raw_data.data where source = \'%s\' and source_id in (\'' + '\',\''.join(doc_ids) + '\')'
    sql = sql % source
    cur.execute(sql)
    exists = dict((r['source_id'], r['draft']) for r in cur.fetchall())

    updated = 0
    inserted = 0

    for doc in docs:
        doc_id = doc.pop('source_id')
        raw = json.dumps(doc.pop('raw'), ensure_ascii=False)
        if doc_id in exists:
            sql = 'update raw_data.data set ' \
                  'raw=%s,' \
                  'draft=%s' \
                  'where source=%s and source_id=%s'
            draft = merge_items([exists[doc_id]] + [doc])
            draft = json.dumps(draft, ensure_ascii=False)
            cur.execute(sql, (raw, draft, source, doc_id))
            updated += 1
        else:
            sql = 'insert into raw_data.data (source_id, source, raw, draft) values (%s,%s,%s,%s)'
            draft = json.dumps(doc, ensure_ascii=False)
            cur.execute(sql, (doc_id, source, raw, draft))
            inserted += 1

    conn.commit()
    cur.close()
    conn.close()

    print "Updated %d docs\nInserted %d docs\n" % (updated, inserted)


if __name__ == '__main__':
    import_data()



