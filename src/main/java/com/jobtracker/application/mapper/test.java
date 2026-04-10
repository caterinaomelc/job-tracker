package com.jobtracker.application.mapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class test {
}

class Solution {
    public boolean containsDuplicate(int[] nums) {

        List<Integer> list = Arrays.stream(nums).boxed().toList();
        for(int i=0;i<nums.length;i++){

            if(Collections.frequency(list, nums[i]) > 1) return true;
        }
        return false;
    }
}