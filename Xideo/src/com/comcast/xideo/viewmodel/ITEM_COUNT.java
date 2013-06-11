package com.comcast.xideo.viewmodel;

import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.collections.ArrayListObservable;

import java.util.List;

public class ITEM_COUNT extends Converter<Integer> {

	public ITEM_COUNT(IObservable<?>[] dependents) {
		super(Integer.class, dependents);
	}

	@Override
	public Integer calculateValue(Object... args) throws Exception {
		int itemCount = 0;
		int len = args.length;
		for (int i = 0; i < len; i++) {
			if (args[i] == null)
				continue;
			if (args[i] instanceof List)
				itemCount += ((List) args[i]).size();
			else if (args[i] instanceof ArrayListObservable) {
				itemCount += ((ArrayListObservable) args[i]).size();
			}
		}
		return itemCount;
	}
}
