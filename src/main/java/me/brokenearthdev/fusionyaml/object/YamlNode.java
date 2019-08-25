/*
Copyright 2019 BrokenEarthDev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package me.brokenearthdev.fusionyaml.object;

import com.google.common.collect.ImmutableList;
import me.brokenearthdev.fusionyaml.utils.StorageUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A {@link YamlNode} is a path that does have child(ren). These child(ren) may as well
 * be a {@link YamlNode}, which houses child(ren), and so on.
 * <pre>
 *     player:
 *       game:
 *         tic-tac-toe: 4
 *         tetris: 0
 *       name: 'steven'
 * </pre>
 * In the example {@code YAML} below, {@code game} and {@code player} is a {@link YamlNode}.
 * {@code game} is a child node of {@code player}
 */
public class YamlNode implements YamlElement {

    /**
     * A {@link Map} containing the node child(ren)
     */
    private Map<String, YamlElement> map = new LinkedHashMap<>();

    /**
     * A {@link List} of child nodes
     */
    private List<YamlNode> childNodes = new LinkedList<>();

    /**
     * Using this constructor will initialize an empty {@link LinkedHashMap}
     * and a {@link List}.
     */
    public YamlNode() {
    }

    /**
     * Using this constructor will set the {@link Map} of {@link String}s and
     * {@link YamlElement}s passed in equal to the {@link Map} initialized outside
     * the constructor's scope.
     * <p>
     * If a value in the {@link Map} is an instance of {@link YamlObject}, the value
     * will be converted to a {@link YamlNode} instance.
     *
     * @param children The map of {@link String}s and {@link YamlElement}s that contains
     *            {@code YAML} data
     */
    public YamlNode(Map<String, YamlElement> children) {
        this.map = children;
        this.childNodes = findChildNodes(children);
    }

    /**
     * Finds child nodes in the {@link Map} passed in.
     *
     * @param map The {@link Map} where the child nodes will be searched for.
     * @return A {@link List} of child nodes in the {@link Map} passed in.
     */
    private List<YamlNode> findChildNodes(Map<String, YamlElement> map) {
        List<YamlNode> list = new LinkedList<>();
        map.forEach((k, v) -> {
            v = (v.isYamlObject()) ? new YamlNode(v.getAsYamlObject().getMap()) : v;
            if (v instanceof YamlNode)
                list.add((YamlNode) v);
        });
        return list;
    }

    /**
     * @return The child(ren) of this node
     */
    public Map<String, YamlElement> getChildren() {
        return map;
    }

    /**
     * Adds a {@link YamlElement} as its child to this {@link YamlNode} object in
     * a given key.
     * <p>
     * If {@link YamlObject} is passed in as the value, the {@link YamlObject} object
     * is converted into {@link YamlNode}.
     * <p>
     * If the value is {@code null}, the key-value pair will get erased. Setting the
     * value equal to {@code null} is equivalent to as calling {@link #removeChild(String)}
     * with the key in the parameter.
     *
     * @param key The key to the value
     * @param element This object's child
     */
    public void addChild(String key, YamlElement element) {
        change(key, element);
    }

    /**
     * Adds a {@link YamlElement} as its child to this {@link YamlNode} object in
     * a given key.
     * <p>
     * If the value is {@code null}, the key-value pair will get erased. Setting the
     * value equal to {@code null} is equivalent to as calling {@link #removeChild(String)}
     * with the key in the parameter.
     *
     * @param key The key to the value
     * @param value This object's child
     */
    public void addChild(String key, String value) {
        change(key, new YamlPrimitive(value));
    }

    /**
     * Adds a {@link YamlElement} as its child to this {@link YamlNode} object in
     * a given key.
     * <p>
     * If the value is {@code null}, the key-value pair will get erased. Setting the
     * value equal to {@code null} is equivalent to as calling {@link #removeChild(String)}
     * with the key in the parameter.
     *
     * @param key The key to the value
     * @param value This object's child
     */
    public void addChild(String key, boolean value) {
        change(key, new YamlPrimitive(value));
    }

    /**
     * Adds a {@link YamlElement} as its child to this {@link YamlNode} object in
     * a given key.
     * <p>
     * If the value is {@code null}, the key-value pair will get erased. Setting the
     * value equal to {@code null} is equivalent to as calling {@link #removeChild(String)}
     * with the key in the parameter.
     *
     * @param key The key to the value
     * @param value This object's child
     */
    public void addChild(String key, Number value) {
        change(key, new YamlPrimitive(value));
    }

    /**
     * Removes the value in a given key. The key-value pair will be erased.
     *
     * @param key The key where its value will be removed.
     *            The key will also be removed.
     */
    public void removeChild(String key) {
        change(key, null);
    }

    /**
     * Changes the {@link Map} containing child key-value pairs appropriately depending
     * on the {@code objects} passed into the parameters
     *
     * @param key The key where the element is found
     * @param element The element (value)
     */
    private void change(String key, YamlElement element) {
        if (element == null)
            map.remove(key);
        else {
            element = (element.isYamlObject()) ? new YamlNode(element.getAsYamlObject().getMap()) : element;
            map.put(key, element);
            if (element instanceof YamlNode)
                childNodes.add((YamlNode) element);
        }
    }

    /**
     * @return Gets the child nodes stored in an {@link com.google.common.collect.ImmutableMap}
     */
    public ImmutableList<YamlNode> getChildNodes() {
        return StorageUtils.toImmutableList(childNodes);
    }

}
