# !/usr/bin/env python3
import os
import sys
from Cryptodome.Hash import SHA256

if len(sys.argv) < 3:
    print("Usage: python3 ", os.path.basename(__file__), "key_file_name document_file_name")
    sys.exit()

key_file_name = sys.argv[1]
file_name = sys.argv[2]

# get the authentication key from the file
# TODO
key = b""
with open(key_file_name, "rb") as f:
    key += f.read()

buf = b""
mac = b""
with open(file_name, "rb") as f:
    mac += f.read(32)
    buf += f.read()

hashh = SHA256.new()
hashh.update(buf)
hashh.update(key)
# Use the remaining file content to generate the message authentication code
# TODO
mac_generated = hashh.digest()

if mac == mac_generated:
    print ('yes')
else:
    print ('no')
