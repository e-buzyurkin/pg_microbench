import argparse
import pandas as pd
import matplotlib.pyplot as plt


def parse_args():
    parser = argparse.ArgumentParser(description="Plot histogram from CSV file")
    parser.add_argument('unit_of_measurement', type=str, help="Unit of measurement")
    parser.add_argument('input_file', type=str, help="Input CSV file")
    parser.add_argument('output_file', type=str, help="Output histogram")
    return parser.parse_args()


def main():
    args = parse_args()

    data = pd.read_csv(args.input_file)

    plt.bar(data['bucket'], data['value'], data['len'])
    plt.xlabel("Latency, " + args.unit_of_measurement)
    plt.ylabel("Amount")

    plt.savefig(args.output_file)
    plt.close()


if __name__ == "__main__":
    main()
