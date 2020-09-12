/**
 * Every class present in this package are relevant to {@link org.fusionyaml.library.reference.Reference}s.
 * A reference is, essentially, a "pointer" to a {@link org.fusionyaml.library.object.YamlElement} and they
 * are have a special syntax.
 * <p>
 * Their syntax is simple. They include the dollar sign, preceding the path to the pointed element.
 * If the element is nested, then a period is used to tell the parser that the pointed element is
 * under a path.
 * <p>
 * Because of our aim of trying to be universal, no part of the FusionYAML library interacts with
 * references. This means that the library will not identify references and return and reference - as if
 * it doesn't exist. Only the user has the power to use {@link org.fusionyaml.library.reference.Reference}s.
 * <p>
 * References are of multiple types:
 * <ol>
 *     <li>ElementReference (reference to a valid yaml element)</li>
 *     <li>NullReference (reference to a null element)</li>
 *     <li>InvalidReference (references that can't be parsed because of some syntax errors)</li>
 * </ol>
 * <p>
 *
 * @see org.fusionyaml.library.reference.References
 */
package org.fusionyaml.library.reference;