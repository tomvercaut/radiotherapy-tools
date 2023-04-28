package tv.radiotherapy.tools.dicom.xml.model;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class UidRegistry implements List<UidRegistry.Item> {


    private final List<Item> il = new ArrayList<>();

    @Override
    public int size() {
        return il.size();
    }

    @Override
    public boolean isEmpty() {
        return il.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return il.contains(o);
    }

    @NotNull
    @Override
    public Iterator<Item> iterator() {
        return il.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return il.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return il.toArray(a);
    }

    @Override
    public boolean add(Item item) {
        return il.add(item);
    }

    @Override
    public boolean remove(Object o) {
        return il.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        //noinspection SlowListContainsAll
        return il.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Item> c) {
        return il.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends Item> c) {
        return il.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return il.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return il.retainAll(c);
    }

    @Override
    public void clear() {
        il.clear();
    }

    @Override
    public Item get(int index) {
        return il.get(index);
    }

    @Override
    public Item set(int index, Item element) {
        return il.set(index, element);
    }

    @Override
    public void add(int index, Item element) {
        il.add(index, element);
    }

    @Override
    public Item remove(int index) {
        return il.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return il.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return il.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<Item> listIterator() {
        return il.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<Item> listIterator(int index) {
        return il.listIterator(index);
    }

    @NotNull
    @Override
    public List<Item> subList(int fromIndex, int toIndex) {
        return il.subList(fromIndex, toIndex);
    }

    @Override
    public void replaceAll(UnaryOperator<Item> operator) {
        il.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super Item> c) {
        il.sort(c);
    }

    @Override
    public Spliterator<Item> spliterator() {
        return il.spliterator();
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return il.toArray(generator);
    }

    @Override
    public boolean removeIf(Predicate<? super Item> filter) {
        return il.removeIf(filter);
    }

    @Override
    public Stream<Item> stream() {
        return il.stream();
    }

    @Override
    public Stream<Item> parallelStream() {
        return il.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super Item> action) {
        il.forEach(action);
    }

    public record Item (
        String uid,
        String name,
        String keyword,
        UidType type,
        OLink link
    ){}
}
