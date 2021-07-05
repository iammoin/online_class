package com.onlineclass.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BaseBody {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        int [] arr = new int[]{16, 17, 4, 3, -1, 2};
        int currMax = -1;
        for(int i = arr.length-1; i>= 0; i--){
            int temp = arr[i];
            if(i == arr.length-1){
                arr[i] = -1;
            } else {
                arr[i] = currMax;
            }

            if(currMax < temp)
                currMax = temp;
        }

        for(int i=0; i<arr.length; i++){
            System.out.print(arr[i] + ",");
        }
    }
}
