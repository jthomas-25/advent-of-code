import re
import sys

class Passport(object):
    def __init__(self):
        self.fields = {}

    def set_field(self, key, value):
        self.fields[key] = value
        
    def has_all_required_fields(self, field_keys):
        for key in field_keys:
            if (key not in self.fields) and (key != "cid"):
                return False
        return True

    def is_valid(self, field_keys):
        if not self.has_all_required_fields(field_keys):
            return False
        for key in self.fields.keys():
            if key == "cid":
                # ignore field
                continue
            value = self.fields[key]
            if key == "byr":
                pattern = "^\d{4}$"
                match = re.search(pattern, value)
                if not match:
                    return False
                value = int(value)
                if value < 1920 or value > 2002:
                    return False
            elif key == "iyr":
                pattern = "^\d{4}$"
                match = re.search(pattern, value)
                if not match:
                    return False
                value = int(value)
                if value < 2010 or value > 2020:
                    return False
            elif key == "eyr":
                pattern = "^\d{4}$"
                match = re.search(pattern, value)
                if not match:
                    return False
                value = int(value)
                if value < 2020 or value > 2030:
                    return False
            elif key == "hgt":
                pattern = "^(?P<height>\d+)(?P<units>cm|in)$"
                match = re.search(pattern, value)
                if not match:
                    return False
                height = int(match.group("height"))
                if match.group("units") == "cm":
                    if height < 150 or height > 193:
                        return False
                elif match.group("units") == "in":
                    if height < 59 or height > 76:
                        return False
            elif key == "hcl":
                pattern = "^#[0-9|a-f]{6}$"
                match = re.search(pattern, value)
                if not match:
                    return False
            elif key == "ecl":
                pattern = "^(amb|blu|brn|gry|grn|hzl|oth)$"
                match = re.search(pattern, value)
                if not match:
                    return False
            elif key == "pid":
                pattern = "^\d{9}$"
                match = re.search(pattern, value)
                if not match:
                    return False
        return True

def main():
    field_keys = ["byr","iyr","eyr","hgt","hcl","ecl","pid","cid"]
    passports = []
    filename = sys.argv[1]
    with open(filename, "r") as batch_file:
        done = False
        while not done:
            line = batch_file.readline()
            passport = Passport()
            done_reading_passport = False
            while not done_reading_passport:
                if line == "\n":
                    # Blank line
                    done_reading_passport = True
                elif line.strip("\n") == "":
                    # EOF
                    done_reading_passport = True
                    done = True
                else:
                    key_value_pairs = line.strip("\n").split(" ")
                    for pair in key_value_pairs:
                        parts = pair.split(":")
                        key = parts[0]
                        value = parts[1]
                        passport.set_field(key, value)
                    line = batch_file.readline()
            passports.append(passport)
    
    print(f"Total number of passports: {len(passports)}")
    num_present = len([p for p in passports if p.has_all_required_fields(field_keys)])
    print(f"Number of valid passports: {num_present}")
    num_present_and_valid = len([p for p in passports if p.is_valid(field_keys)])
    print(f"Number of valid passports: {num_present_and_valid}")

if __name__ == "__main__":
    main()
