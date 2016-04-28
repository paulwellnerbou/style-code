package de.wellnerbou.stylecode.app;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.Cli;
import com.lexicalscope.jewel.cli.CliFactory;
import de.wellnerbou.stylecode.StyleCodeBuilder;

import java.io.IOException;

public class StyleCodeApp {

    public static void main(final String[] args) throws IOException {
        final Cli<CliOptions> cli = CliFactory.createCli(CliOptions.class);
        try {
            final CliOptions options = cli.parseArguments(args);
            runStyleCode(options);
        } catch (ArgumentValidationException e) {
            System.out.println(e.getMessage());
            System.out.println(cli.getHelpMessage());
        }
    }

    private static void runStyleCode(CliOptions options) throws IOException {
        new StyleCodeBuilder(options.markdownFile())
                .useStylesAndScriptsFrom(options.getUrlToFetchResourcesFrom())
                .excludeResourcesMatching(options.excludePatterns())
                .includeInlineScripts(options.includeInlineScripts())
                .withIframeHtmlTemplate(options.useIframeTemplate())
                .withIndexHtmlTemplate(options.useIndexTemplate())
                .build().generate(options.getOutputDirectory());
    }
}
