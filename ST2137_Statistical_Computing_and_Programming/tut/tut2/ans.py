import pandas as pd
import numpy as np
import re

def fn1(str1):
    out_string = re.split('[LD]+', str1)
    st1 = [len(x) for x in out_string]
    return st1



data = pd.read_csv("liverpool_2223_season.csv")

print(f"Number of dates = {len(set(data["Date"]))}")

data['result'] = np.where(data['GF'] > data['GA'], 'W', np.where(data['GF'] == data['GA'], "D", "L"))
data['pts'] = np.where(data['GF'] > data['GA'], 3, np.where(data['GF'] == data['GA'], 1, 0))

for x in set(data["Opponent"]):
    print(f"Points against {x}: {sum(data[data["Opponent"] == x]["pts"])}")

data["total_pts"] = data['pts'].cumsum()
print(data["total_pts"].iloc[len(data) - 1])

win = data["result"].str.cat()
rle_out = fn1(win)
print(np.max(rle_out))
