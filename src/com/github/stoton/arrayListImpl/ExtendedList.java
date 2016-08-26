package com.github.stoton.arrayListImpl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class ExtendedList<T> implements Iterable<T> {

    private int size = 0;
    private Class<T> classType;
    private T workArray[];

    public ExtendedList(Class<T> classType) {
	workArray = (T[]) Array.newInstance(classType, 0);
	this.size = 0;
	this.classType = classType;
    }

    public ExtendedList(Class<T> classType, int size) {
	workArray = (T[]) Array.newInstance(classType, size);
	this.size = size;
    }

    public void add(T arg0) {
	extendCapacity(size++);
	T supportArray[] = (T[]) Array.newInstance(classType, size());
	supportArray = Arrays.copyOf(workArray, size());
	supportArray[size - 1] = arg0;
	workArray = Arrays.copyOf(supportArray, size());
    }

    public T get(int index) {
	rangeCheck(index);
	return workArray[index];
    }

    public void clear() {
	workArray = (T[]) Array.newInstance(classType, 0);
    }

    public int size() {
	return size;
    }

    public void set(int index, T arg0) {
	rangeCheck(index);
	workArray[index] = arg0;
    }

    public T[] getList() {
	return workArray;
    }

    public int find(T arg0) {
	for (int i = 0; i < size(); i++) {
	    if (arg0 != null && workArray[i] != null && workArray[i].equals(arg0))
		return i;
	}
	return -1;
    }

    public int countOfElements(T arg0) {
	int count = 0;
	for (T t : workArray) {
	    if (t != null && arg0 != null && t.equals(arg0))
		count++;
	}
	return count;
    }

    @SuppressWarnings("hiding")
    public <T extends Comparable<? super T>> void sort() {
	Arrays.sort(this.getList());
    }

    public void reverse() {
	for (int i = 0; i < workArray.length / 2; i++) {
	    T temp = workArray[i];
	    workArray[i] = workArray[workArray.length - i - 1];
	    workArray[workArray.length - i - 1] = temp;
	}
    }

    public T[] copyToArray(int begin, int end) {
	rangeCheck(begin);
	rangeCheck(end - 1);

	begin = Math.min(begin, end);
	end = Math.max(begin, end);

	T[] array = (T[]) Array.newInstance(classType, end);

	for (int i = begin; i < end; i++) {
	    array[i] = workArray[i];
	}
	return array;
    }

    public ArrayList<T> copyToArrayList(int begin, int end) {
	rangeCheck(begin);
	rangeCheck(end - 1);

	begin = Math.min(begin, end);
	end = Math.max(begin, end);
	List<T> list = new ArrayList<T>();
	for (int i = begin; i < end; i++) {
	    list.add(workArray[i]);
	}
	return (ArrayList<T>) list;
    }

    public LinkedList<T> copyToLinkedList(int begin, int end) {
	rangeCheck(begin);
	rangeCheck(end - 1);

	begin = Math.min(begin, end);
	end = Math.max(begin, end);

	List<T> list = new LinkedList<T>();
	for (int i = begin; i < end; i++) {
	    list.add(workArray[i]);
	}
	return (LinkedList<T>) list;
    }

    public boolean compareToOtherList(List<T> listA) {
	List<T> listB = copyToArrayList(0, workArray.length);
	return listA.containsAll(listB) && listB.containsAll(listA);
    }

    public boolean compareToOtherList(ExtendedList<T> listA) {
	if (!indexOutRange(listA.size(), workArray.length))
	    return false;

	ExtendedList<T> listB = copyToExtendedlist(0, workArray.length);
	return listA.containsAll(listB) && listB.containsAll(listA);
    }

    private ExtendedList<T> copyToExtendedlist(int begin, int end) {
	rangeCheck(begin);
	rangeCheck(end - 1);

	begin = Math.min(begin, end);
	end = Math.max(begin, end);

	ExtendedList<T> extendedList = new ExtendedList<T>(classType);

	for (int i = begin; i < end; i++) {
	    extendedList.add(workArray[i]);
	}
	return extendedList;
    }

    public boolean containsAll(ExtendedList<T> extendedList) {

	int length = Math.min(extendedList.size(), workArray.length);

	for (int i = 0; i < length; i++) {
	    if (extendedList.find(workArray[i]) == -1)
		return false;
	}
	return true;
    }

    public boolean regex(int index, String regex) {
	rangeCheck(index);
	if (classType.isInstance(regex) || workArray[0] instanceof Number || workArray[0] instanceof CharSequence) {
	    String pattern = String.valueOf(workArray[index]);
	    Pattern validate = Pattern.compile(regex);
	    Matcher matcher = validate.matcher((CharSequence) pattern);
	    return matcher.matches();
	}
	return false;
    }

    private void rangeCheck(int index) {
	if (index >= this.size() || index < 0)
	    throw new IndexOutOfBoundsException();
    }

    public void forEach(Consumer<? super T> action) {
	for (int i = 0; i < workArray.length; i++)
	    action.accept(workArray[i]);
    }

    public void extendCapacity(int minimalCapacity) {
	int oldCapacity = workArray.length;
	if (minimalCapacity > oldCapacity) {
	    int newCapacity = (oldCapacity * 3) / 2 + 1;
	    if (newCapacity < minimalCapacity)
		newCapacity = minimalCapacity;
	    workArray = Arrays.copyOf(workArray, newCapacity);
	}
    }

    private boolean indexOutRange(int a, int b) {
	return a == b;
    }

    @Override
    public Iterator<T> iterator() {
	return new Iterator<T>() {

	    private int current;

	    @Override
	    public boolean hasNext() {
		return current < workArray.length;
	    }

	    @Override
	    public T next() {
		if (!hasNext())
		    throw new NoSuchElementException();
		return workArray[current++];
	    }
	};
    }
}
