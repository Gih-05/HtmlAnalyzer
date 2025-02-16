import java.io.*; //ler e escrever dados
import java.net.*; //conexão de rede e URLs
import java.util.*; //estrutura de dados (ex. Stack/pilha)


// MÉTODO "main": Início -> verifica URL, chama métodos p/ baixar e analisar HTML
public class HtmlAnalyzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HtmlAnalyzer <url>");
            return;
        }

        String url = args[0];
        try {
            String htmlContent = fetchHtml(url);
            String deepestText = extractDeepestText(htmlContent);
            System.out.println(deepestText);
        } catch (MalformedURLException e) {
            System.out.println("URL malformed error");
        } catch (IOException e) {
            System.out.println("URL connection error");
        } catch (Exception e) {
            System.out.println("malformed HTML");
        }
    }

    //MÉTODO "fetchHtml": Baixa o conteúdo HTML da URL
    private static String fetchHtml(String urlString) throws IOException {
        StringBuilder content = new StringBuilder();
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        while ((line = in.readLine()) != null) {
            content.append(line.trim()).append("\n");
        }
        in.close();
        return content.toString();
    }

    //MÉTODO "extractDeepestText": Encontra o texto mais profundo do HTML analisado
    private static String extractDeepestText(String html) throws Exception {
        String[] lines = html.split("\n");
        Stack<String> tagStack = new Stack<>();
        int maxDepth = -1;
        String deepestText = "";
        boolean foundText = false;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            if (isOpeningTag(line)) {
                tagStack.push(line);
            } else if (isClosingTag(line)) {
                tagStack.pop();
            } else if (isText(line)) {
                int currentDepth = tagStack.size();
                if (currentDepth > maxDepth) {
                    maxDepth = currentDepth;
                    deepestText = line;
                    foundText = true;
                }
            }
        }

        if (!foundText) {
            throw new Exception("Nenhum texto encontrado no HTML.");
        }

        return deepestText;
    }

    //MÉTODOS AUXILIARES: Facilitam a lógica de analise -> verifica-se se cada linha é tag de abertura, fechamento ou texto
    private static boolean isOpeningTag(String line) {
        return line.startsWith("<") && !line.startsWith("</") && line.endsWith(">");
    }

    private static boolean isClosingTag(String line) {
        return line.startsWith("</") && line.endsWith(">");
    }

    private static boolean isText(String line) {
        return !line.startsWith("<") && !line.endsWith(">");
    }

}

//Abrir terminal e compilar o código: javac HtmlAnalyzer.java
//Executar com uma URL válida: java HtmlAnalyzer https://example.com