import java.util.Scanner;
import java.util.Arrays;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class RadixSort {
    // counter for the number of primitive operations(Assumption: Each function call for non-user defined function counts as 1 primitive operation)
    public static int opCounter = 0;
    //This function compares between the current maximum integer with each integer in the array
    //When the for loop reaches the end of the array, it the maximum integer number in the array is found and returned to the calling instance
    static int returnMaxVal(BigDecimal[]unsortedList, int len) {
        opCounter += 1; // 1 assignment, max = 0
        int max = 0;
        opCounter += 1; // 1 assignment. i = 0
        for(int i=0; i < len; i++) {
            opCounter += 1; // 1 comparison (i < arrayLen)
            opCounter += 2; // 1 comparison (max < array[i]), 1 array reference (array[i])
            if (unsortedList[i].intValue() > max){
                opCounter += 2; // 1 assignment, 1 array reference
                max = unsortedList[i].intValue();
            }
            opCounter += 2; // 1 assignment, 1 arithmetic operation(i++)
        }
        opCounter += 1; // 1 return
        return max;
    }

    //This function performs counting sort algorithm on the array integers according to the divisor
    static void countSortAlgorithm(BigDecimal[]unsortedList, int len, int divisor, int[]array1, int[]array2, int maxValue, int maxValueDigit, BigDecimal powerOfTen) {

        //Initialize a tally[] array, akin to 10 buckets for 10 different digits(0-9)
        int tally[] = new int[10];
        opCounter += 1; // 1 assignment

        //Define each integer in the array as zero
        Arrays.fill(tally, 0);
        opCounter += 1; // 1 function call

        //Initialize and define a counter (cnt) as zero
        int cnt = 0;
        opCounter += 1; // 1 assignment

        //Initialize and define number of zeroes that a divisor has as zero
        int zeroes = 0;
        opCounter += 1; // 1 assignment

        //This while loop computes the number of zeroes that a divisor has
        opCounter += 3; // 1 arithmetic operation(%), 2 comparisons(==, !=),
        while(divisor%10 == 0 && divisor != 0) {
            //zeroes is incremented by one for every zero that a divisor has
            zeroes++;
            opCounter += 2; // 1 assignment, 1 arithmetic operation(+)

            //divisor is divided and truncated by 10 each loop
            divisor /= 10;
            opCounter += 2; // 1 assignment, 1 arithmetic operation(/)

            //counter (cnt) increments by one each loop to store the total number of times that a while loop has executed
            cnt++;
            opCounter += 2; // 1 assignment, 1 arithmetic operation(+)

            opCounter += 3; // 1 arithmetic operation(%), 2 comparisons(==, !=),
        }

        //Calculate the original value of divisor based on the counter (cnt)
        opCounter += 1; // 1 assignment(i=0)
        for(int i=0; i < cnt; i++){
            opCounter += 1; // 1 comparison
            divisor *= 10;
            opCounter += 2; // 1 assignment, 1 arithmetic operation(*)
            opCounter += 2; // 1 assignment, 1 arithmetic operation(i++)
        }

        //Conditional statements that set array1[] or array2[] as the source or destination based on the value of zeroes
        //The if statement will execute initially to transfer and sort the integers from unsortedList[] array to array1[]
        //unsortedList[] array is the source, array1[] is the destination
        opCounter += 1; // 1 comparison
        if(zeroes == 0) {

            //Compute and record the number of instances of an index position in the array
            opCounter += 1; // 1 assignment
            for (int i = 0; i < len; i++){
                opCounter += 1; // 1 comparison
                tally[(unsortedList[i].intValue() / divisor) % 10]++;
                opCounter += 6; // 2 array reference(tally[], unsortedList[]), 3 arithmetic operation(/, %, +), 1 assignment
                opCounter += 2; // 1 assignment, 1 arithmetic operation(i++)
            }


            //Compute the true position of an integer in the array to be outputted
            opCounter += 1; // 1 assignment
            for (int i = 1; i < 10; i++){
                opCounter += 1; // 1 comparison
                opCounter += 5; // 2 array reference, 2 arithmetic operation(+, -), 1 assignment
                tally[i] += tally[i - 1];
                opCounter += 2; // 1 assignment, 1 arithmetic operation(i++)
            }


            //Form the output array, in this case, the destination array is array1[]
            opCounter += 2; // 1 assignment, 1 arithmetic operation
            for (int i = len - 1; i >= 0; i--)
            {
                opCounter += 1; // 1 comparison
                //Insert and append the integer from unsortedList[] to array1[] based on the numbers determined in the tally[] array
                array1[tally[(unsortedList[i].intValue() / divisor) % 10] - 1] = unsortedList[i].intValue();
                opCounter += 8; // 4 array reference(array1[], tally[], unsortedList[], unsortedList[]), 3 arithmetic operation(/, %, -), 1 assignment

                //After each insertion, decrement the number at the corresponding position in the tally[] array
                tally[(unsortedList[i].intValue() / divisor) % 10]--;
                opCounter += 6; // 2 array reference(tally[], unsortedList[]), 3 arithmetic operation(/, %, -), 1 assignment

                opCounter += 2; // 1 assignment, 1 arithmetic operation(i--)
            }

            //print the sorted array
            //use String.format to pad the numbers according to the max number of digits in the array
            //Reference: https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#format-java.lang.String-java.lang.Object...-
            System.out.print("\n(Pass " + (zeroes+1) + ") \n");
            System.out.print("Array 1: \n");

            // cycle through 0 to 9 to print out the array according to their arrangement in the bucket
            opCounter += 1; // 1 assignment
            for (int i = 0; i < 10; i++) {
                opCounter += 1; // 1 comparison
                System.out.print(String.format("%4s", i+ "->"));

                opCounter += 1; // 1 assignment
                for (int j = 0; j < len; j++){
                    opCounter += 1; // 1 comparison
                    // print out the elements according to the buckets they belong to
                    // (array1[j]/divisor)%10 is the nth digit of the elements starting from the right, where n is the number of passes
                    // for example, in pass 2, the value of divisor is 10. (array1[j]/10)%10 will return the 2nd last digit
                    // this allows us to print out all the elements that belong in the buckets for each pass
                    opCounter += 3; // 1 array reference, 2 arithmetic operation(/, %), 1 comparison
                    if ((array1[j]/divisor)%10 == i) {
                        // use string formatting to left pad the number with spaces according to the highest number of digits in the original, unsorted array
                        System.out.print(String.format("%" + (maxValueDigit + 2) + "s", new BigDecimal(array1[j]).divide(powerOfTen)));
                    }
                }
                System.out.print("\n");
            }
        }

        //The else if statement will execute is the value of zeroes is even
        //array2[] is the source, array1[] is the destination

        else if(zeroes % 2 == 0) {
            opCounter += 2; // 1 arithmetic operation, 1 comparison (moved downward to avoid obstructing the if, else if statement)
            //Compute and record the number of instances of an index position in the array
            opCounter += 1; // 1 assignment
            for (int i = 0; i < len; i++){
                opCounter += 1; // 1 comparison
                tally[(array2[i] / divisor) % 10]++;
                opCounter += 6; // 2 array reference(tally[], unsortedList[]), 3 arithmetic operation(/, %, +), 1 assignment
                opCounter += 2; // 1 assignment, 1 arithmetic operation(i++)
            }

            //Compute the true position of an integer in the array to be outputted
            opCounter += 1; // 1 assignment
            for (int i = 1; i < 10; i++) {
                opCounter += 1; // 1 comparison
                opCounter += 5; // 2 array reference, 2 arithmetic operation(+, -), 1 assignment
                tally[i] += tally[i - 1];
                opCounter += 2; // 1 assignment, 1 arithmetic operation(i++)
            }

            //Form the output array, in this case, the destination array is array1[]
            opCounter += 2; // 1 assignment, 1 arithmetic operation
            for (int i = len - 1; i >= 0; i--)
            {
                //Insert and append the integer from array2[] to array1[] based on the numbers determined in the tally[] array
                opCounter += 1; // 1 comparison
                array1[tally[(array2[i] / divisor) % 10] - 1] = array2[i];
                opCounter += 8; // 4 array reference(array1[], tally[], unsortedList[], unsortedList[]), 3 arithmetic operation(/, %, -), 1 assignment
                //After each insertion, decrement the number at the corresponding position in the tally[] array
                tally[(array2[i] / divisor) % 10]--;
                opCounter += 6; // 2 array reference(tally[], unsortedList[]), 3 arithmetic operation(/, %, -), 1 assignment
            }

//            //print the sorted array without showing the bucket(commented out)
//            System.out.print("(Pass " + (zeroes+1) + ") \n");
//            System.out.print("Array 1: ");
//            for (int i = 0; i < len; i++)
//                System.out.print(array1[i] + " ");
//            System.out.print("\n\n");
            System.out.print("\n(Pass " + (zeroes+1) + ") \n");
            System.out.print("Array 1: \n");
            // cycle through 0 to 9 to print out the array according to their arrangement in the bucket
            opCounter += 1; // 1 assignment
            for (int i = 0; i < 10; i++) {
                opCounter += 1; // 1 comparison
                System.out.print(String.format("%4s", i+ "->"));
                opCounter += 1; // 1 assignment
                for (int j = 0; j < len; j++){
                    opCounter += 1; // 1 comparison
                    // print out the elements according to the buckets they belong to
                    // (array1[j]/divisor)%10 is the nth digit of the elements starting from the right, where n is the number of passes
                    // for example, in pass 2, the value of divisor is 10. (array1[j]/10)%10 will return the 2nd last digit
                    // this allows us to print out all the elements that belong in the buckets for each pass
                    opCounter += 3; // 1 array reference, 2 arithmetic operation(/, %), 1 comparison
                    if ((array1[j]/divisor)%10 == i) {
                        // use string formatting to left pad the number with spaces according to the highest number of digits in the original, unsorted array
                        System.out.print(String.format("%" + (maxValueDigit + 2) + "s", new BigDecimal(array1[j]).divide(powerOfTen)));
                    }
                }
                System.out.print("\n");
            }
        }

        //The else statement will execute if the value of zeroes is odd
        //array1[] is the source, array2[] is the destination
        else {

            //Compute and record the number of instances of an index position in the array
            opCounter += 1; // 1 assignment
            for (int i = 0; i < len; i++){
                opCounter += 1; // 1 comparison
                tally[(array1[i] / divisor) % 10]++;
                opCounter += 6; // 2 array reference(tally[], unsortedList[]), 3 arithmetic operation(/, %, +), 1 assignment
                opCounter += 2; // 1 assignment, 1 arithmetic operation(i++)
            }


            //Compute the true position of an integer in the array to be outputted
            opCounter += 1; // 1 assignment
            for (int i = 1; i < 10; i++){
                opCounter += 1; // 1 comparison
                tally[i] += tally[i - 1];
                opCounter += 5; // 2 array reference, 2 arithmetic operation(+, -), 1 assignment
                opCounter += 2; // 1 assignment, 1 arithmetic operation(i++)
            }

            //Form the output array, in this case, the destination array is array1[]
            opCounter += 2; // 1 assignment, 1 arithmetic operation
            for (int i = len - 1; i >= 0; i--)
            {
                opCounter += 1; // 1 comparison
                //Insert and append the integer from array1[] to array2[] based on the numbers determined in the tally[] array
                array2[tally[(array1[i] / divisor) % 10] - 1] = array1[i];
                opCounter += 8; // 4 array reference(array1[], tally[], unsortedList[], unsortedList[]), 3 arithmetic operation(/, %, -), 1 assignment
                //After each insertion, decrement the number at the corresponding position in the tally[] array
                tally[(array1[i] / divisor) % 10]--;
                opCounter += 6; // 2 array reference(tally[], unsortedList[]), 3 arithmetic operation(/, %, -), 1 assignment

                opCounter += 2; // 1 assignment, 1 arithmetic operation(i--)
            }

//            //print the sorted array without showing the bucket(commented out)
//            System.out.print("(Pass " + (zeroes+1) + ") \n");
//            System.out.print("Array 2: ");
//            for (int i = 0; i < len; i++)
//                System.out.print(array2[i] + " ");
//            System.out.print("\n\n");
            System.out.print("\n(Pass " + (zeroes+1) + ") \n");
            System.out.print("Array 2: \n");

            // cycle through 0 to 9 to print out the array according to their arrangement in the bucket
            opCounter += 1; // 1 assignment
            for (int i = 0; i < 10; i++) {
                opCounter += 1; // 1 comparison
                System.out.print(String.format("%4s", i+ "->"));
                opCounter += 1; // 1 assignment
                for (int j = 0; j < len; j++){
                    opCounter += 1; // 1 comparison
                    // print out the elements according to the buckets they belong to
                    // (array1[j]/divisor)%10 is the nth digit of the elements starting from the right, where n is the number of passes
                    // for example, in pass 2, the value of divisor is 10. (array1[j]/10)%10 will return the 2nd last digit
                    // this allows us to print out all the elements that belong in the buckets for each pass
                    opCounter += 4; // 1 array reference, 2 arithmetic operation(/, %), 1 comparison
                    if ((array2[j]/divisor)%10 == i) {
                        // use string formatting to left pad the number with spaces according to the highest number of digits in the original, unsorted array
                        System.out.print(String.format("%" + (maxValueDigit + 2) + "s", new BigDecimal(array2[j]).divide(powerOfTen)));
                    }
                }
                System.out.print("\n");
            }
        }
    }

    //This function conducts radix sort algorithm
    static void radixSortAlgorithm(BigDecimal[]unsortedList, int len, int[]array1, int[]array2) {

        // Obtaiting the maximum value of decimal value for float inputs
        int maxDecimalPlaces = 0;
        opCounter += 1; // 1 assignment
        for (BigDecimal num : unsortedList) {
            opCounter += 1; // 1 comparison
            int decimalPlaces = num.scale();
            opCounter += 2; // 1 assignment, 1 function call
            if (decimalPlaces > maxDecimalPlaces) {
                opCounter += 1; // 1 comparison
                maxDecimalPlaces = decimalPlaces;
                opCounter += 1; // 1 assignment
            }
        }

        // Multiply each BigDecimal value by 10 raised to the power of the max decimal places
        BigDecimal powerOfTen = new BigDecimal("10").pow(maxDecimalPlaces);
        opCounter += 2; // 1 assignment, 1 arithmetic operation
        opCounter += 1; // 1 assigment
        for (int i = 0; i < len; i++) {
            opCounter += 2; // 1 comparison, 1 arithmetic operation
            unsortedList[i] = unsortedList[i].multiply(powerOfTen);
            opCounter += 2; // 1 assignment, 1 arithmetic operation
        }

        //Define the maximum value and its number of digit as zero initially
        int maxValue = 0;
        opCounter += 1; // 1 assignment
        int maxValueDigit = 0;
        opCounter += 1; // 1 assignment

        //Obtain the maximum value from the unsorted array of integer numbers along with its number of digits
        maxValue = returnMaxVal(unsortedList, len);
        opCounter += 2; // 1 function call, 1 assignment
        maxValueDigit = String.valueOf(maxValue).length();
        opCounter += 3; // 2 function call(valueOf(), length()), 1 assignment

        opCounter += 1; // 1 assignment
        for(int divisor=1; (maxValue / divisor) > 0; divisor *= 10)
        {
            opCounter += 2; // 1 arithmetic operation, 1 comparison
            //Each for loop calls a countSortAlgorithm to perform counting sort on the array sequence
            countSortAlgorithm(unsortedList, len, divisor, array1, array2, maxValue, maxValueDigit, powerOfTen);
            opCounter += 1; // 1 function call
            opCounter += 2; // 1 assignment, 1 arithmetic operation
        }
        System.out.print("Sorted list: ");
        opCounter += 1; // 1 function call
        // The number of passes and the final array destination(1 or 2) depends on the highest number of digit of element in the array
        // For example, if the highest number of digits is 5, then there will be 5 passes, and the final array destination is array 1
        opCounter += 2; // 1 arithmetic operation, 1 comparison
        if (maxValueDigit % 2 == 1) {
            opCounter += 1; // 1 assignment
            for (int i = 0; i < len; i++){
                opCounter += 1; // 1 comparison
                System.out.print(new BigDecimal(array1[i]).divide(powerOfTen) + " ");
                opCounter += 2; // 1 arithmetic operation, 1 assignment(i++)
            }
        }
        else {
            opCounter += 1; // 1 assignment
            for (int i = 0; i < len; i++){
                opCounter += 1; // 1 comparison
                System.out.print(new BigDecimal(array2[i]).divide(powerOfTen) + " ");
                opCounter += 2; // 1 arithmetic operation, 1 assignment(i++)
            }
        }
        System.out.print("\n\n");
    }

    public static void main(String[] args) {

        //Define an array that stores a certain length of integer numbers
        Scanner input =new Scanner(System.in);

        // Input the number of elements
        System.out.print("Enter the number of elements to be sorted: ");
        // The length of the unsortedList[] array is stored in the variable len
        int len = input.nextInt();
        while (len <= 0){
            System.out.println("The number of elements must be > 0\n");

            System.out.print("Enter the number of elements to be sorted: ");
            len = input.nextInt();
        }

        // Set the number of digits for integer part
        System.out.print("Enter the number of digit(s) in the integer part: ");
        int numIntDigits = input.nextInt();

        // Set the number of digits for decimal part
        System.out.print("Enter the number of digit(s) in the decimal part: ");
        int numDecDigits = input.nextInt();
        while (numIntDigits <= 0 && numDecDigits <= 0){
            System.out.println("One of the digit part must be > 0\n");

            System.out.print("Enter the number of digit(s) in the integer part: ");
            numIntDigits = input.nextInt();

            System.out.print("Enter the number of digit(s) in the decimal part: ");
            numDecDigits = input.nextInt();
        }

        // Input the array elements
        BigDecimal[] unsortedList = new BigDecimal[len];

        Random random = new Random();

        for (int i = 0; i < unsortedList.length; i++) {
            BigDecimal randomValue;

            // Check if the number of digit in the integer part is == 0 or not.
            // if yes, proceed with the original random number generator
            if (numIntDigits == 0){
                do {
                    randomValue = BigDecimal.valueOf(random.nextInt((int) Math.pow(10, numIntDigits)))
                            .add(new BigDecimal(random.nextDouble())
                                    .setScale(numDecDigits, RoundingMode.HALF_DOWN));
                } while (randomValue.toBigInteger().toString().length() < numIntDigits);
                unsortedList[i] = randomValue;
            }
            // if not, the max value limit for the random number generator is decreased by 1 to avoid integers with one extra digit.
            else {
                do {
                    randomValue = BigDecimal.valueOf(random.nextInt((int) Math.pow(10, numIntDigits)-1))
                            .add(new BigDecimal(random.nextDouble())
                                    .setScale(numDecDigits, RoundingMode.HALF_DOWN));
                } while (randomValue.toBigInteger().toString().length() < numIntDigits);
                unsortedList[i] = randomValue;
            }
        }

        System.out.print("\nUnsorted List: ");
        for (int i = 0; i < len; i++){
            System.out.print((unsortedList[i]) + " ");
        }

        //Initialize two arrays with data type integer
        //These arrays have the same length as the unsortedList[] array from which the numbers will be transferred to be sorted
        int array1[] = new int[len];
        int array2[] = new int[len];

        //Define each integer in the array as zero
        Arrays.fill(array1, 0);
        Arrays.fill(array2, 0);

        //Call upon the radixSortAlgorithm function to perform radix sort
        //Three arrays and a variable that stores the length of the unsortedList[] array are passed
        radixSortAlgorithm(unsortedList, len, array1, array2);
        System.out.println("Number of primitive operations: " + opCounter);
    }
}