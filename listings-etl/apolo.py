import json

users = set()
with open(r'C:\Users\mikhail\Documents\PSafe\temp\users.txt', 'r') as f:
    for l in f:
        users.add(l.strip())

counters = {}
users_unique = set()
with open(r'C:\Users\mikhail\Documents\PSafe\temp\out', 'r', encoding='utf8') as f:
    count = 0
    for l in f:
        count += 1
        if not count % 10000:
            print(count)

        j = json.loads(l.split('\t')[1])
        imei = j['imei']
        if imei in users:
            users_unique.add(imei)
            version = '.'.join(j['version'].split('.')[:2])
            if version not in counters:
                counters[version] = set()
            counters[version].add(imei)

for v, u in counters.items():
    print('%s count=%d' % (v, len(u)))

print('unique' + str(len(users_unique)))