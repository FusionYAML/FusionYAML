package me.brokenearthdev.simpleyaml;

import me.brokenearthdev.simpleyaml.entities.YamlMapper;

import java.util.Map;

public class Test {

    public static void main(String[] args) {
        YamlMapper mapper = new YamlMapper("hiasdasdasd:\n    o: 2\n    s: 2323");
        Map<?, ?> obj = mapper.map();
        System.out.println(obj);
        System.out.println("DONE");
    }

}
