package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Serde <T> {

    String serialize(T object);

    T deserialize(String arg);

    static <T> Serde<T> of(Function<T, String> serializer, Function<String, T> deserializer) {
        return new Serde<T>() {
            @Override
            public String serialize(T object) {
                return serializer.apply(object);
            }

            @Override
            public T deserialize(String arg) {
                return deserializer.apply(arg);
            }
        };
    }

    static <T> Serde<T> oneOf(List<T> allValues){
        return of(t -> String.valueOf(allValues.indexOf(t)), s -> allValues.get(Integer.parseInt(s)));
    }

//    static <T> Serde<List<T>> listOf(Serde<T> serde, String separator){
//        return new Serde<List<T>>() {
//
//            @Override
//            public String serialize(List<T> object) {
//                String[] serialized = new String[object.size()];
//                for (int i = 0; i < object.size(); i++) {
//                    serialized[i] = serde.serialize(object.get(i));
//                }
//                return String.join(separator, serialized);
//            }
//
//            @Override
//            public List<T> deserialize(String arg) {
//                List<T> deserialized = new ArrayList<>();
//                for(String s : arg.split(Pattern.quote(separator), -1)){
//                    deserialized.add(serde.deserialize(s));
//                }
//                return deserialized;
//            }
//        };
//    }

    static <T> Serde<List<T>> listOf(Serde<T> serde, String separator){
        return new Serde<>() {

            @Override
            public String serialize(List<T> object) {
                return object.stream()
                        .map(serde::serialize)
                        .collect(Collectors.joining(separator));
            }

            @Override
            public List<T> deserialize(String arg) {
                return Stream.of(arg.split(Pattern.quote(separator), -1))
                        .map(serde::deserialize)
                        .collect(Collectors.toList());
            }
        };
    }

    static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, String separator){
        Serde<List<T>> listSerde = listOf(serde, separator);
        return of(s -> listSerde.serialize(s.toList()), t -> SortedBag.of(listSerde.deserialize(t)));
    }
}
