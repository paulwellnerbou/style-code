# style-code
A java based alternative to tools like [StyleDocco](https://jacobrask.github.io/styledocco/),
[documentCSS](http://documentcss.com/), [KSS Stylesheets](http://warpspire.com/kss/styleguides/) or [Devbridge's Styleguide](http://livingstyleguide.devbridge.com/), but without
the (mostly node.js, npm and Ruby or even Ruby Dev Kit related) dependencies those tools bring with it. In addition, style-code does not focus on CSS code (or SCSS/SASS/Less) but markdown.

This makes this tool suitable for a highly automated and platform independent environment
(means: runs even on windows, if you really want to, without any installation issues, just a JVM is required) and usable as
well for legacy project with an already existing CSS hell: the documentation is not written into the productive CSS files, but
into a completely separate markdown file.

Another use case might be tutorials or documentation with examples for HTML, CSS and JavaScript related technologies.

## Usage

### Gradle project

Of course, you can use this application via command line. The recommended way of usage is by setting up a gradle project using
the [StyleCode Gradle Plugin](https://github.com/paulwellnerbou/style-code-gradle-plugin), though.
See [this demo project](https://github.com/paulwellnerbou/style-code-demo-project) as an implementation reference.

### Command line

You can get the latest distribution release [Github's releases page](https://github.com/paulwellnerbou/style-code/releases).

```
wget https://github.com/paulwellnerbou/style-code/releases/download/v0.3/stylecode-0.3.zip
unzip stylecode*.zip
./stylecode-*/bin/stylecode
```

This will output the help:

```
Option must have a value: ARGUMENTS
Usage: [options] ARGUMENTS
        [--additional-resources value...] : Any other resources which should be included additionally. If you don't have an URL to fetch the resources from you can give the resources here.
        [--exclude value...] : Resources found in the HTML of the URL given in --resources-from which should not be included (String.contains() is used here):
        [--include-inline-scripts] : Wether to include inline JavaScripts found in the HTML of the URL given in --resources-from.
        [--out value] : Directory the output should be written to.
        [--resources-from value] : URL to fetch resources (CSS, JS) from.
        [--iframe-template value] : Mustache template file to use for embedded iframes with HTML demos. If you still want resources (<script...>, <style>, <link rel=stylesheet...>) to be included, use {{headerresources}} for resources included in HTML header and {{bodyresources}} for resources included in the HTML body.
        [--index-template value] : Mustache template file to use for the resulting index.html.
```

#### Example usage:

```
./build/install/stylecode/bin/stylecode --resources-from=http://paul.wellnerbou.de --out=./out/ src/test/resources/markdown/paulwellnerbou.md
```

You can then open `./out/index.html` with any browser.

## Building from source

### Building the library jar

You may want to add the `install` task, if you want the artifact deployed in your local maven repository, if not, skip it.

```
./gradlew clean build install
```

### Building the command line app

```
./gradlew clean installDist
```

### Deploying to bintray

```
./gradlew bintrayUpload -PpublishUser=<USER> -PpublishKey=<KEY>
```
