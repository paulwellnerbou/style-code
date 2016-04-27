package de.wellnerbou.stylecode.app;

import com.lexicalscope.jewel.cli.Option;
import com.lexicalscope.jewel.cli.Unparsed;

import java.util.List;

public interface CliOptions {

    @Option(defaultToNull = true)
    String getUrlToFetchResourcesFrom();

    @Option(defaultValue = "false")
    boolean includeInlineScripts();

    @Option(defaultValue = {})
    List<String> excludePatterns();

    @Option(defaultValue = "out")
    String getOutputDirectory();

    @Unparsed
    String markdownFile();
}
