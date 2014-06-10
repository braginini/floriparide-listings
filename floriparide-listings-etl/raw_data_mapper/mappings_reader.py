import traceback
def getmap(filepath):
    """
    opens and reads attribute or rubric mapping files
    and produces map that contains hagah name as a key and id as value
    """
    map = {}
    try:
        f = open(filepath, encoding="utf-8");
        try:
            #skip header
            f.readline()
            for line in f:
              line = line.replace("\n","")
              split = line.split(";")
              if len(split) > 1 and split[1] != "":
                id = split[0]
                list = split[1:len(split)]
                for name in list:
                    map[name] = id
        finally:
            f.close()
    except IOError:
        print(traceback.format_exc())
    return map