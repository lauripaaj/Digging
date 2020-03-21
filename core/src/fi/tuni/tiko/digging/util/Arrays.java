/**
 * Arrays class contains useful functions to make process of modifying arrays faster and
 * clearer.
 *
 * Method "toIntArray (String[])" takes array containing Strings and turns them into integers in an int array,
 * which will be returned. The Strings need to contain numbers only for it to work (or a - in front of them as in case of negative numbers).
 *
 * If every String in the method does not contain numbers that can be converted into
 * integer, the method will not work as intended. It will
 * print out an error message while letting the incomplete array be returned anyway.
 *
 *
 * Method "contains" takes first parameter integer (value), and checks if it's identical to any
 * of the indexes in the second parameter (array of integer values). The method will return boolean
 * value True in this case, and otherwise it will return False.
 *
 *
 * Method "containsSameValues" checks how many of the integers in the first array (first
 * parameter), have the same value as any of the indexes in another array (second parameter).
 *
 * Important: It is expected that both of the arrays contain only unique values.
 *
 * For example arrays [1, 2, 3, 4, 5, 6, 7] and [4, 10, 21, 22, 23, 1, 25] would return the
 * value 2, since the numbers 4 and 1 are in both of the arrays.
 *
 *
 * Method "addPrefix" (int[], int) takes int array as first parameter and returns a String containing value
 * of each of its indexes, while seperating them with comma "," and space " " and also adding bracket "["
 * as the first character in a string, and "]" as the last one. If there is
 * only one index in it, there will be no comma or space included. The second parameter (int) will determine
 * the amount of numbers each part(index turned into part of the String) will hold, and if it's set out to be bigger than
 * the current number, there will be "0":s added as prefixes to it to fill the set number.
 *
 * For example int array of {2, 911, 35} with a proposed number of 4 given as second parameter will return the following String:
 * "[0002, 0911, 0035]".
 *
 * If proposed number is not 0, but negative or smaller or equal to the longest number in the int array turned into String, the amount
 * of numbers will be set auomatically according to the longest number in the array. So the same array with a proposed number of -1 or 2
 * for example, would print out the following String:
 * "[002, 911, 035]" since 911 is the longest of the indexes.
 *
 * If proposed number is 0, the same array would not contain any of the prefixes, but just the brackets:
 * "[2, 911, 35]"
 *
 * If the int array contains negative numbers, they will be added to the final String as well, but they are not taken into account when
 * determining the length of the array. For example int array of {-2, 35, -911,  9} with a proposed number of 5 would print out as:
 * "[-00002, 00035, -00911, 00009]"
 *
 *
 *
 *@author Lauri Paajarvi
 */


package fi.tuni.tiko.digging.util;

import javax.lang.model.util.ElementScanner6;


public class Arrays {
    public static int [] toIntArray(String [] array) {
        int [] intArray = new int[array.length];

        for (int i=0; i<intArray.length; i++) {
            try {
                intArray[i]=Integer.parseInt(array[i]);
            } catch (Exception e) {
                System.out.println("Error. method toIntArray couldn't be completed. Additional info about error: "+ e );
            }
        }
        return intArray;
    }
    public static boolean contains(int value, int [] array) {
        boolean found=false;
        for (int i=0; i<array.length; i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return found;
    }

    public static int containsSameValues(int [] array1, int [] array2) {
        int amount = 0;
        for (int i=0; i<array1.length; i++) {
            if (contains(array1[i], array2)) {
                amount++;
            }
        }
        return amount;
    }

    public static String toString(int[] intArray) {
        String arrayAsString="";
        if (intArray.length > 1) {
            for (int i=0; i<intArray.length-1; i++) {
                arrayAsString += intArray[i]+", ";
            }
            arrayAsString += intArray[intArray.length-1];
        } else {
            arrayAsString += intArray[0];
        }
        return arrayAsString;
    }

    public static int[] removeIndex(int [] original, int index) {
        int [] newer = new int[original.length-1];

        if (index >= 0 && index < original.length) {

            for (int i=0; i<index; i++) {
                newer[i]=original[i];
            }
            for (int i=index+1; i<original.length; i++) {
                newer[i-1]=original[i];
            }
        } else {
            newer = new int[original.length];
            newer=original;
        }
        return newer;
    }

    // copy-pasted from https://www.geeksforgeeks.org/selection-sort/
    public static void sort(int arr[]) {

        int n = arr.length;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (arr[j] < arr[min_idx])
                    min_idx = j;

            // Swap the found minimum element with the first
            // element
            int temp = arr[min_idx];
            arr[min_idx] = arr[i];
            arr[i] = temp;
        }
    }
    public static String addPrefix(int [] intArray, int proposedNumberLength) {
        /*if numberLength (given as parameter) is less than 0, or if it's smaller
        than the longest number in the array, the size will be set automatically so
        there will be the same amount of numbers in every "index", as there are in the longest number
        in the array.
        */
        String toBeReturned="[";
        /*if proposedNumberLength is 0, there will be no prefixes, but the brackets will still be there, so array {1, 2, 234, 86}
        would be printed out as [1, 2, 234, 86]
        */
        if (proposedNumberLength==0) {
            toBeReturned+=Arrays.toString(intArray)+"]";
        } else {
            /*Java.lang.Math is used to make sure negative values will be taken into account too, but the minus "-" character, won't be
            taken into account when the number of prefixes is determined
            */
            int numberLength;
            int maxValue=java.lang.Math.abs(intArray[0]);
            for (int i=0; i<intArray.length; i++) {
                if (java.lang.Math.abs(intArray[i])>maxValue) {
                    maxValue=java.lang.Math.abs(intArray[i]);
                }
            }
            int maxLength=Integer.toString(maxValue).length();
            if (proposedNumberLength<maxLength) {
                numberLength=maxLength;
            } else {
                numberLength=proposedNumberLength;
            }
            for (int arrayIndex=0; arrayIndex<intArray.length; arrayIndex++) {
                String valueOfIndex=Integer.toString(java.lang.Math.abs(intArray[arrayIndex]));
                int noOfZeroes=numberLength-valueOfIndex.length();

                if (intArray[arrayIndex]<0) {
                    toBeReturned += "-";
                }
                for (int zeroCounter=0; zeroCounter<noOfZeroes; zeroCounter++) {
                    toBeReturned += "0";
                }
                toBeReturned += valueOfIndex;
                if (arrayIndex<(intArray.length-1)) {
                    toBeReturned += ", ";
                } else {
                    toBeReturned += "]";
                }
            }
        }
        return toBeReturned;
    }

}
