// Java program to find minimum time required to
// send people on other side of bridge
import java.io.*;

class GFG {
	/* Global dp[2^20][2] array, in dp[i][j]--
		'i' denotes mask in which 'set bits' denotes
		total people standing at left side of bridge
		and 'j' denotes the turn that represent on
		which side we have to send people either
		from left to right(0) or from right to
		left(1) */
	static int dp[][] = new int[1 << 20][2];

	/* Utility function to find total time required
	to send people to other side of bridge */
	public static int findMinTime(int leftmask,
								boolean turn, int arr[],
								int n)
	{

		// If all people has been transferred
		if (leftmask == 0)
			return 0;

		int res = dp[leftmask][turn == true ? 1 : 0];

		// If we already have solved this subproblem,
		// return the answer.
		if (~res != 0)
			return res;

		// Calculate mask of right side of people
		int rightmask = ((1 << n) - 1) ^ leftmask;

		/* if turn == 1 means currently people are at
		right side, thus we need to transfer
		people to the left side */
		if (turn == true) {
			int minRow = Integer.MAX_VALUE, person = 0;
			for (int i = 0; i < n; ++i) {

				// Select one people whose time is less
				// among all others present at right
				// side
				if ((rightmask & (1 << i)) != 0) {
					if (minRow > arr[i]) {
						person = i;
						minRow = arr[i];
					}
				}
			}

			// Add that person to answer and recurse for
			// next turn after initializing that person at
			// left side
			res = arr[person]
				+ findMinTime(leftmask | (1 << person),
								!turn, arr, n);
		}
		else {

			// __builtin_popcount() is inbuilt gcc function
			// which will count total set bits in 'leftmask'
			if (Integer.bitCount(leftmask) == 1) {
				for (int i = 0; i < n; ++i) {

					// Since one person is present at left
					// side, thus return that person only
					if ((leftmask & (1 << i)) != 0) {
						res = arr[i];
						break;
					}
				}
			}
			else {

				// try for every pair of people by
				// sending them to right side

				// Initialize the result with maximum value
				res = Integer.MAX_VALUE;
				for (int i = 0; i < n; ++i) {

					// If ith person is not present then
					// skip the rest loop
					if ((leftmask & (1 << i)) == 0)
						continue;

					for (int j = i + 1; j < n; ++j) {
						if ((leftmask & (1 << j)) != 0) {

							// Find maximum integer(slowest
							// person's time)
							int val
								= Math.max(arr[i], arr[j]);

							// Recurse for other people
							// after un-setting the ith and
							// jth bit of left-mask
							val += findMinTime(
								(leftmask ^ (1 << i)
								^ (1 << j)),
								!turn, arr, n);
							// Find minimum answer among
							// all chosen values
							res = Math.min(res, val);
						}
					}
				}
			}
		}
		return res;
	}

	// Utility function to find minimum time
	public static int findTime(int arr[], int n)
	{
		// Find the mask of 'n' peoples
		int mask = (1 << n) - 1;

		// Initialize all entries in dp as -1
		for (int i = 0; i < (1 << 20); i++) {
			dp[i][0] = -1;
			dp[i][1] = -1;
		}

		return findMinTime(mask, false, arr, n);
	}

	// Driver Code
	public static void main(String[] args)
	{
		int arr[] = { 10, 20, 30 };
		int n = 3;
		System.out.print(findTime(arr, n));
	}
}

// This code is contributed by Pankaj Gupta
