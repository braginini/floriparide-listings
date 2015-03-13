__author__ = 'mikhail'


with (open(u'C:\\Users\\mikhail\\Documents\\PSafe\\temp\\2015-02-07-22_26ea00c0-f426-4629-8ffc-a7678b356d41', encoding='utf-8')) as f:
    line = f.readline()
    print(line)
    if "\'\'" in line:
        print(line)
    # if len(line.split(',')) > 16:
    #     print(line)

