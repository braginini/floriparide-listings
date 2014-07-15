import traceback


def getmap(file_path):
    """
    opens and reads attribute or rubric mapping files
    and produces map that contains hagah name as a key and id as value
    """
    map = {}
    try:
        f = open(file_path, encoding="utf-8")
        try:
            for line in f:
                split = line.split(";")
                name = split[0]
                address = split[3]
                map[name + address] = True
        finally:
            f.close()
    except IOError:
        print(traceback.format_exc())
    return map