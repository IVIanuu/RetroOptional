/*
 * Copyright 2017 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.retrooptional;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.ivianuu.retrooptional.Preconditions.checkNotNull;

/**
 * Wraps an object which present or not
 */
public final class Option<T> {

    private static final Option<?> ABSENT = new Option<>();

    private final T value;

    private Option() {
        value = null;
    }

    private Option(T value) {
        checkNotNull(value, "value == null");
        this.value = value;
    }

    /**
     * Returns a present option
     */
    @NonNull public static <T> Option<T> of(@NonNull T value) {
        checkNotNull(value, "value == null");
        return new Option<>(value);
    }

    /**
     * Returns a maybe present option
     */
    @NonNull public static <T> Option<T> ofNullable(@Nullable T value) {
        if (value == null) {
            return of(value);
        } else {
            return absent();
        }
    }

    /**
     * Returns a absent option
     */
    @NonNull public static <T> Option<T> absent() {
        return (Option<T>) ABSENT;
    }

    /**
     * Returns whether the value is present or not
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Returns the value of this option
     * Throws a npe if the value is not present
     */
    @NonNull public T get() {
        checkNotNull(value, "value == null");
        return value;
    }

    /**
     * Returns a wrapped object obtained from a function if performed on a present wrapper.
     */
    @NonNull public <S> Option<S> map(@NonNull Function1<? super T, S> function) {
        checkNotNull(function, "function == null");
        if (isPresent()) {
            return of(function.apply(value));
        } else  {
            return absent();
        }
    }

    /**
     * Returns a wrapped object obtained from a function if performed on a present wrapper.
     */
    @NonNull public <S> Option<S> flatMap(@NonNull Function1<? super T, Option<S>> mapper) {
        checkNotNull(mapper, "mapper == null");
        if (isPresent()) {
            Option<S> mapped = mapper.apply(value);
            checkNotNull(mapped, "mapped == null");
            return mapped;
        }

        return absent();
    }

    /**
     * If the predicate evaluates to true, returns the instance, otherwise returns an absent
     * wrapper.
     */
    @NonNull public Option<T> filter(@NonNull Predicate<? super T> predicate) {
        if (isPresent()
                && predicate.test(value)) {
            return this;
        } else {
            return absent();
        }
    }

    /**
     * Returns the value if present or else the other one
     */
    @NonNull public T or(@NonNull T other) {
        checkNotNull(other, "other == null");
        return isPresent() ? value : other;
    }

    /**
     * Returns the value if present or else the one from the supplier
     */
    @NonNull public T or(@NonNull Supplier<T> supplier) {
        checkNotNull(supplier, "supplier == null");
        if (isPresent()) {
            return value;
        } else  {
            T value = supplier.get();
            checkNotNull(value, "value == null");
            return value;
        }
    }

    /**
     * Returns the value of this option or null
     */
    @Nullable public T orNull() {
        return value;
    }

    /**
     * Returns the value if present or throws the exception
     */
    @NonNull public <X extends Throwable> T orThrow(@NonNull X throwable) throws X {
        checkNotNull(throwable, "throwable == null");

        if (isPresent()) {
            return value;
        }

        throw throwable;
    }

    /**
     * Returns the value if present or throws the exception from the supplier
     */
    @NonNull public <X extends Throwable> T orThrow(@NonNull Supplier<? extends X> throwableSupplier) throws X {
        checkNotNull(throwableSupplier, "throwableSupplier == null");

        if (isPresent()) {
            return value;
        }

        throw throwableSupplier.get();
    }

    /**
     * Invokes the consumer if the value is present
     */
    public void ifPresent(@NonNull Consumer<T> consumer) {
        checkNotNull(consumer, "consumer == null");
        if (isPresent()) {
            consumer.consume(value);
        }
    }

    /**
     * Invokes the function if value is absent
     */
    public void ifAbsent(@NonNull Function function) {
        checkNotNull(function, "function == null");
        if (!isPresent()) {
            function.call();
        }
    }

    /**
     * Consumes the value if present or calls the function
     */
    public void ifPresentOrElse(@NonNull Consumer<T> consumer, @NonNull Function function) {
        checkNotNull(consumer, "consumer == null");
        checkNotNull(function, "function == null");
        if (isPresent()) {
            consumer.consume(value);
        } else {
            function.call();
        }
    }

    /**
     * Returns the casted value if present and possible otherwise a absent option
     */
    public <S> Option<S> asType() {
        if (isPresent()) {
            try {
                return Option.of((S) value);
            } catch (ClassCastException e) {
                return absent();
            }
        } else {
            return absent();
        }
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Option)) {
            return false;
        }

        Option<?> other = (Option<?>) o;
        return (value == null) ? (other.value == null) : value.equals(other.value);
    }

    @Override public int hashCode() {
        return isPresent() ? value.hashCode() : 0;
    }

    @Override public String toString() {
        return isPresent() ? String.format("Option[%s]", value) : "Option.empty";
    }
}
