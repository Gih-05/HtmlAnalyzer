Para uma leitura mais limpa do código, 
as explicações sobre cada parte do código principal encontram-se neste arquivo.

Dessa forma ao sentir dúvidas sobre qualquer método, comando ou chamado
é possível utilizar este arquivo como consulta.


/*IMPORTS DE BIBLIOTECAS*/
import java.io.*; //ler e escrever dados
import java.net.*; //conexão de rede e URLs
import java.util.*; //estrutura de dados (ex. Stack/pilha)


/* MÉTODO "MAIN"
Garante que o programa não continue sem uma URL válida. Se o número de argumentos for
diferente de 1, exibe uma mensagem de uso e encerra o programa.
Controla o fluxo do programa, trata erros e exibe mensagens adequadas em caso de problemas.
*/
public class HtmlAnalyzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HtmlAnalyzer <url>");
            return;
        }


        /* Controla o fluxo do programa, trata erros e 
        exibe mensagens adequadas em caso de problemas. */

        String url = args[0];
        try {
            String htmlContent = fetchHtml(url); //Tenta baixar o conteúdo HTML
            String deepestText = extractDeepestText(htmlContent); //Extrai o texto mais profundo do HTML
            System.out.println(deepestText);
        } catch (MalformedURLException e) {
            System.out.println("URL malformed error");
        } catch (IOException e) {
            System.out.println("URL connection error");
        } catch (Exception e) {
            System.out.println("malformed HTML");
        }
    }


/* MÉTODO "FETCHHTML"
Configura a conexão e prepara para ler o conteúdo HTML.
Baixa o conteúdo HTML da URL e o retorna como uma string.
*/

    private static String fetchHtml(String urlString) throws IOException {
        StringBuilder content = new StringBuilder(); //objeto para armazenar conteúdo HTML.
        URL url = new URL(urlString); //Cria um objeto URL a partir da string fornecida.
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        //Abre uma conexão com a URL e cria um BufferedReader para ler o conteúdo.

        //Baixa o conteúdo HTML da URL e o retorna como uma string. Lê linha por linha
        String line;
        while ((line = in.readLine()) != null) { 
            content.append(line.trim()).append("\n");
        } /*remove os espaços em branco no início e final de cada linha (trim) e 
            adiciona (append) cada linha ao StringBuilder.*/

        in.close();
        return content.toString(); //fecha o BufferedReader e retorna o conteúdo HTML como string
    }

/* MÉTODO "EXTRACT DEEPEST TEXT"
Prepara as estruturas necessárias para processar o HTML;
Prepara cada linha para análise;
Analisa o HTML e encontra o texto mais profundo;
Finaliza a análise e retorna o resultado.
*/
    private static String extractDeepestText(String html) throws Exception {
        String[] lines = html.split("\n"); //divide conteúdo em linhas
        Stack<String> tagStack = new Stack<>(); //cria uma pilha para rastrear tags de abertura
        int maxDepth = -1; //variável para armazenar profundidaded máxima
        String deepestText = ""; //variável para armazenar texto mais profundo
        boolean foundText = false; //verifica se algum texto foi encontrado


        for (String line : lines) {   //itera (repete) sobre cada linha do HTML
            line = line.trim(); //remove espaços em branco no início e no fim da linha
            if (line.isEmpty()) //ignora linhas vazias
                continue;


            if (isOpeningTag(line)) {
                tagStack.push(line);  //se a linha for uma tag de abertura, adiciona à pilha
            } else if (isClosingTag(line)) {
                tagStack.pop();  //se for uma tag de fechamento, remove a última tag de abertura da pilha
            
            } else if (isText(line)) {   //se for texto, verifica se está no nível mais profundo e armazena
                int currentDepth = tagStack.size();
                if (currentDepth > maxDepth) {
                    maxDepth = currentDepth;
                    deepestText = line;
                    foundText = true;
                }
            }
        }

        /*verifica se algum texto foi encontrado, 
          se nenhum for encontrado, lança uma exceção
        */
        if (!foundText) {
            throw new Exception("Nenhum texto encontrado no HTML.");
        }

        return deepestText; //retorna o texto mais profundo
    }
    //finaliza a análise e retorna o resultado.


/* MÉTODOS AUXILIARES
Simplifica a lógica de análise do HTML, verificando se é uma tag de:
abertura, fechamento ou texto.
*/
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


/* RESUMO
main: Início. Faz a verificação da URL, chama os métodos para baixar, 
analisa o HTML e exibe o resultado.

fetchHtml: Baixa o conteúdo HTML de uma URL

extractDeepestText: Analisa o HTML para encontrar o texto mais profundo

Métodos Auxiliares: Verificam o tipo de cada linha (tag de abertura, fechamento ou texto)
*/