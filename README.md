EASTER_EGG_URLS

# HtmlAnalyzer

## Description

HtmlAnalyzer is a Java command-line program that analyzes an HTML document
obtained from a URL and returns the first text found at the deepest level
of the HTML structure.

The program also validates whether the HTML structure is well-formed,
according to the constraints defined in the challenge.

---

## Functional Requirements

The program follows these rules:

- The HTML is processed line by line;
- Each line is one of the following:
  - Opening tag (`<div>`)
  - Closing tag (`</div>`)
  - Text content (`This is the body.`);
- A line never contains more than one type of content;
- Only paired tags are used (no self-closing tags like `<br/>`);
- Opening tags do not contain attributes;
- Leading spaces and blank lines are ignored;
- If multiple text lines are found at the maximum depth, the first one is returned;
- If the HTML structure is invalid, the program returns `malformed HTML`.

---

## Technical Requirements

- Developed in Java using JDK 17;
- No external libraries or frameworks are used;
- No use of HTML, XML or DOM parsing libraries;
- Compiles with:

```bash
$ javac HtmlAnalyzer.java
```
- Runs with:

```bash
# Execute this command in the directory after compilation
$ java HtmlAnalyzer (url)

# Replacing the (url) for a real URL for testing
$ java HtmlAnalyzer http://hiring.axreng.com/internship/example1.html
```

## How It Works

1. The program receives a URL as a command line argument;
2. An HTTP connection is opened to retrieve the HTML content;
3. The HTML is read line by line using BufferedReader;
4. A stack is used to track opening and closing tags;
5. A depth counter is updated as tags are opened and closed;
6. Every time a text line is found, its depth is compared with the current maximum;
7. The first text found at the deepest level is stored;
8. At the end, the program validates if all tags were properly closed;
9. The result is printed to standard output.

### Output

The program prints exactly one of the following:

- The deepest text found in the HTML;
- `malformed HTML` if the HTML structure is invalid;
- `URL connection error` if the URL cannot be accessed.

No additional output is produced.

[Example HTML](http://hiring.axreng.com/internship/example1.html)
```html
<html>
  <head>
    <title>
      This is the title.
    </title>
  </head>
  <body>
    This is the body.
  </body>
</html>
```

**Response:**
```bash
This is the title
```

## References

- [geeksforgeeks](https://www.geeksforgeeks.org/java/java-net-urlconnection-class-in-java/) - Java URL Connection Class
- [geeksforgeeks](https://www.geeksforgeeks.org/java/stack-class-in-java/) - Stack in Java
- [geeksforgeeks](https://www.geeksforgeeks.org/java/java-io-bufferedreader-class-java/) - BufferedReader Class in Java
