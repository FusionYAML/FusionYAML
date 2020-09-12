package org.fusionyaml.library.utils;

import org.fusionyaml.library.FusionYAML;
import org.yaml.snakeyaml.DumperOptions;

class ChoiceConstants {
    
    static final FusionYAML.Builder F_DEFAULT_YAML = new FusionYAML.Builder();
    
    static final FusionYAML.Builder F_PRETTY_YAML = new FusionYAML.Builder()
            .flowStyle(DumperOptions.FlowStyle.FLOW)
            .prettyFlow(true);
    static final FusionYAML.Builder F_ONLY_EXPOSED = new FusionYAML.Builder()
            .onlyExposed(true);
    static final FusionYAML.Builder F_EXCLUDE_NULL = new FusionYAML.Builder()
            .onlyExposed(true);
    static final FusionYAML.Builder F_ONLY_ENUM_NAME_MENTIONED = new FusionYAML.Builder()
            .enumNameMentioned(true);
}
