package group3.ssd.blockchain.p2p.Node;


// STOLEN FROM:

// https://cr.openjdk.org/~vadim/8140503/webrev.01/modules/base/src/main/java/javafx/util/Pair.java.html

/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */


import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <p>A convenience class to represent name-value pairs.</p>
 *
 * @since JavaFX 2.0
 */
public class Pair<K, T> implements Serializable {

    private K key;

    private T value;

    private Pair(K key, T value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public <S> Pair<S, T> mapKey(Function<K, S> function) {
        return Pair.of(function.apply(getKey()), getValue());
    }

    public <S> Pair<K, S> mapValue(Function<T, S> function) {
        return Pair.of(getKey(), function.apply(getValue()));
    }

    public <S> S apply(BiFunction<K, T, S> function) {
        return function.apply(getKey(), getValue());
    }

    public static <K, T> Pair<K, T> of(K key, T value) {
        return new Pair<>(key, value);
    }

    @Override
    public String toString() {
        return "{" + key.toString() + ", " + value.toString() + "}";
    }
}



