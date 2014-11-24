__author__ = 'Mike'

def get_map(file_path):
    """
    opens and reads attribute or rubric mapping files
    and produces resilt_map that contains hagah name as a key and id as value
    """

    with open(file_path, encoding="utf-8") as f:
        resilt_map = {}
        # skip header
        f.readline()
        for line in f:
            line = line.replace("\n", "")
            split = line.split(";")
            if len(split) > 1 and split[1] != "":
                id = split[0]
                list = split[1:len(split)]
                for name in list:
                    resilt_map[name] = id
    return resilt_map