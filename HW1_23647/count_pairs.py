import ctypes
import platform

class BigInt(ctypes.Structure):
    _fields_ = [
        ("size", ctypes.c_int),
        ("sign", ctypes.c_int),
        ("digits", ctypes.POINTER(ctypes.c_int))
    ]

def read_file(filename: str) -> tuple[list[int], int]:
    with open(filename) as file:
        target = int(file.readline())
        n = int(file.readline())
        return ([int(file.readline()) for _ in range(n)], target)

def count_pairs_file(filename: str) -> int:
    data, target = read_file(filename)

    # Load the C library 
    if platform.system() == 'Windows':
        lib = ctypes.CDLL('./libbigint.dll')
    elif platform.system() == 'Linux':
        lib = ctypes.CDLL('./libbigint.so')
    else:
        raise OSError("Unsupported operating system")
    

    # Convert Python list to C BigInt array
    c_data = (ctypes.POINTER(BigInt) * len(data))()  # Create an array of BigInt pointers
    for i, num in enumerate(data):
        bigint_ptr = lib.bigint_create(1, 1 if num >= 0 else -1)  # Create a BigInt object
        lib.bigint_set_int(bigint_ptr, num)
        c_data[i] = bigint_ptr

    # Create BigInt for target
    target_bigint = lib.bigint_create(1, 1 if target >= 0 else -1)
    lib.bigint_set_int(target_bigint, target)

    # **Algorithm for counting pairs:**
    count = 0
    for i in range(len(data) - 1):
        for j in range(i + 1, len(data)):
            # Use C function for BigInt difference
            diff_ptr = lib.bigint_subtract(c_data[i], c_data[j])

            # Compare difference with target
            cmp = lib.bigint_compare(diff_ptr, target_bigint)
            if cmp == 0:
                count += 1

            # Free memory allocated for temporary BigInt object
            lib.bigint_destroy(diff_ptr)

    # Free the memory allocated for BigInt objects in c_data
    for bigint_ptr in c_data:
        lib.bigint_destroy(bigint_ptr)
    lib.bigint_destroy(target_bigint)

    return count
