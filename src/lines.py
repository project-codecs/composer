import os
from statistics import median

def endswithin(s, suffixes):
    return any(s.endswith(suffix) for suffix in suffixes)

def anyin(s, parts):
    return any(part in s for part in parts)

def avg_lines(files):
    total = sum(lines for _, lines, _ in files)
    return total // len(files) if files else 0

def count_lines_in_file(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as file:
            return sum(1 for _ in file)
    except (UnicodeDecodeError, OSError):
        return 0

def find_files_with_line_counts(directory, count_json=False):
    # Default extensions
    extensions = [".java", ".fsh", ".vsh", ".lua"]
    if count_json:
        extensions += [".json", ".jsonc"]

    files = []
    for root, _, filenames in os.walk(directory):
        for filename in filenames:
            # Skip logo.jsonc always
            if anyin(root, ["src", "scripts", ".vscode"]): # ----------------------------------------------------------
                continue
            if endswithin(filename, extensions):
                filepath = os.path.join(root, filename)
                line_count = count_lines_in_file(filepath)
                relative_path = os.path.relpath(filepath, directory)
                files.append((filename, line_count, relative_path))
    return sorted(files, key=lambda x: x[1], reverse=True)

def display_table(files):
    total_lines = sum(line_count for _, line_count, _ in files)
    file_count = len(files)
    average = avg_lines(files)
    line_counts = [lines for _, lines, _ in files]
    filetypes = {".java": 0, ".fsh": 0, ".vsh": 0, ".json": 0, ".jsonc": 0, ".lua": 0}

    for name, _, _ in files:
        for ext in filetypes:
            if name.endswith(ext):
                filetypes[ext] += 1

    largest = files[0] if files else ("N/A", 0, "")
    non_zero = [f for f in files if f[1] > 0]
    smallest = non_zero[-1] if non_zero else ("N/A", 0, "")
    med = median(line_counts) if line_counts else 0

    print("{:<40} {:<20} {:<50}".format("File Name", "Lines", "Relative Path"))
    print("=" * 90)
    for file, line_count, relative_path in files:
        print("{:<40} {:<20} {:<50}".format(file, line_count, relative_path))
    print("=" * 90)
    print(f"Total files     : {file_count}")
    print(f"Total lines     : {total_lines}")
    print(f"Average lines   : {average}")
    print(f"Median lines    : {med}")
    print(f".java files     : {filetypes['.java']}")
    print(f".fsh files      : {filetypes['.fsh']}")
    print(f".vsh files      : {filetypes['.vsh']}")
    print(f".lua files      : {filetypes['.lua']}")
    if ".json" in filetypes:
        print(f".json files     : {filetypes['.json']}")
    if ".jsonc" in filetypes:
        print(f".jsonc files    : {filetypes['.jsonc']}")
    print(f"Largest file    : {largest[0]} ({largest[1]} lines)")
    print(f"Smallest (non-zero) file: {smallest[0]} ({smallest[1]} lines)")

if __name__ == "__main__":
    current_directory = os.getcwd()
    files = find_files_with_line_counts(current_directory, count_json=False)
    display_table(files)
