import psycopg2
import psycopg2.extras

__author__ = 'mikhail'

source = 'hagah'
with open(r'C:\tools\cygwin\home\mikhail\hagah_companies.json', 'r', encoding='utf8') as f:
    with psycopg2.connect(
            'dbname=floriparide_listings user=postgres password=postgres host=localhost port=5432') as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as curr:
            to_insert = list()
            query = 'INSERT INTO raw_data.data (source, data) VALUES (%s, %s::json)'
            for raw_line in f:
                to_insert.append((source, raw_line))
                if len(to_insert) >= 1000:
                    curr.executemany(query, to_insert)
                    conn.commit()
                    to_insert.clear()

            if to_insert:
                curr.executemany(query, to_insert)







