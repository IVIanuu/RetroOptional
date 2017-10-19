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

package com.ivianuu.retrooptional.kotlin

import com.ivianuu.retrooptional.Option

/**
 * Returns a option of the value
 */
fun <T> optionOf(value: T) = Option.of(value)

/**
 * Returns a option of the nullable value
 */
fun <T> optionOfNullable(value: T?) = Option.ofNullable(value)

/**
 * Returns a absent option
 */
fun <T> absent() = Option.absent<T>()

/**
 * Returns this value as a option
 */
fun <T> T.toOption() = Option.ofNullable(this)