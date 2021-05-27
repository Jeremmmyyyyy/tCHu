package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generic Interface used to create functions that serialize/deserialize objects into a String
 *
 * @author Jérémy Barghorn (328403)
 * @author Yann Ennassih (329978)
 */

public interface Serde <T> {

    /**
     * Serializes an object in the form of a String
     * @param object to be serialized
     * @return the serialized String
     */
    String serialize(T object);

    /**
     * Deserializes a string
     * @param arg string to be deserialized
     * @return the deserialized object
     */
    T deserialize(String arg);

    /**
     * Creates an instance of Serde with given serializing/deserializing functions
     * @param serializer the function used to serialize an object
     * @param deserializer the function used to deserialize the String
     * @param <T> type of the object to serialize
     * @return an instance of Serde
     */
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

    /**
     * Creates a Serde of a list of given elements
     * @param allValues List of all the elements
     * @param <T> type of the elements in the List
     * @return a Serde for this list
     */
    static <T> Serde<T> oneOf(List<T> allValues){
        return of(t -> String.valueOf(allValues.indexOf(t)), s -> allValues.get(Integer.parseInt(s)));
    }

    /**
     * Creates a Serde that is able to (de)serialize lists of values that are (de)serialized by the given serde
     * @param serde that serialize the list
     * @param separator that is put in between each element of the list
     * @param <T> type of the elements in the list
     * @return a Serde for this list of elements
     */
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
                return arg.isEmpty() ? List.of() : Stream.of(arg.split(Pattern.quote(separator), -1))
                        .map(serde::deserialize)
                        .collect(Collectors.toList());
            }

        };

    }

    /**
     * Creates a Serde that is able to (de)serialize SortedBags of values that are (de)serialized by the given serde
     * @param serde that serialize the SortedBag
     * @param separator that is put in between each element of the list
     * @param <T> type of the elements in the SortedBag
     * @return a Serde for this SortedBag of elements
     */
    static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, String separator){
        Serde<List<T>> listSerde = listOf(serde, separator);
        return of(s -> listSerde.serialize(s.toList()), t -> SortedBag.of(listSerde.deserialize(t)));
    }


}
