import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv("hist.csv")

plt.bar(data['bucket'], data['value'], data['len'])
plt.xlabel('Latency, ns')
plt.ylabel('Amount')
plt.show()
