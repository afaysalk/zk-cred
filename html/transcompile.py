filename = "credential_verify.html"  

with open(filename, "r") as file:
    lines = file.readlines()

quoted_lines = ['"' + line.strip().replace('"', r'\"') + '"+' for line in lines]

for line in quoted_lines:
    print(line)

    
    