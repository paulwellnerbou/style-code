package de.wellnerbou.styledoc;

import com.google.common.io.ByteStreams;
import org.junit.Test;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
public class MarkdownParsingTest {

	@Test
	public void testMarkdownParse() throws URISyntaxException, IOException {
		PegDownProcessor pegDownProcessor = new PegDownProcessor(Extensions.ALL);

		try (final InputStream in = this.getClass().getResourceAsStream("/markdown/test.md")) {
			final String markdownString = new String(ByteStreams.toByteArray(in));
			final String html = pegDownProcessor.markdownToHtml(markdownString);

			try (PrintWriter out = new PrintWriter("test.html")) {
				out.println(html);
			}
		}
	}
}
