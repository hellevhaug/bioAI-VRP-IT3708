import numpy as np
import matplotlib.pyplot as plt
import json

with open('routes.txt') as f:
    lines = f.readlines()
    trainInstanceIndex = eval(lines[0])
    routes = eval(lines[1])

path = "src/train/train_" + str(trainInstanceIndex) + ".json"
f = open(path, "r")
data = json.loads(f.read())
patients = data["patients"]

depot = np.array([data["depot"]["x_coord"], data["depot"]["y_coord"]])

for route in routes:
    if(len(route) > 0):
        array = np.zeros(shape=(len(route)+2, 2))
        array[0] = depot
        for i, patient in enumerate(route):
            array[i+1] = [patients[str(patient)]["x_coord"], patients[str(patient)]["y_coord"]]
        array[-1] = depot
        plt.plot(array[:, 0], array[:, 1])
plt.show()