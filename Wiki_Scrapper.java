package wikiPedia;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiScraper {
    private static final String WIKI_PREFIX = "https://en.wikipedia.org/wiki/";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Accept a Wikipedia link
        System.out.print("Enter a Wikipedia link: ");
        String inputLink = scanner.nextLine().trim();
        if (!isValidWikiLink(inputLink)) {
            throw new IllegalArgumentException("Invalid Wikipedia link");
        }
        Set<String> wikiLinks = new HashSet<>();
        wikiLinks.add(inputLink);

        // Step 2: Accept an integer between 1 to 20
        System.out.print("Enter a valid integer between 1 to 20: ");
        int n = scanner.nextInt();
        if (n < 1 || n > 20) {
            throw new IllegalArgumentException("Invalid integer");
        }

        // Step 3-4: Scrape the link for all wiki links embedded in the page
        for (int i = 0; i < n; i++) {
            Set<String> newWikiLinks = new HashSet<>();
            for (String link : wikiLinks) {
                URL url = new URL(link);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    for (String wikiLink : extractWikiLinks(line)) {
                        newWikiLinks.add(wikiLink);
                    }
                }
            }
            wikiLinks.addAll(newWikiLinks);
        }

        // Step 5: Print the scraped wiki links
        System.out.println("Scraped Wiki Links:");
        for (String link : wikiLinks) {
            System.out.println(link);
        }
    }

    // Helper method to check if the link is a valid Wikipedia link
    private static boolean isValidWikiLink(String link) {
        return link.startsWith(WIKI_PREFIX);
    }

    // Helper method to extract all wiki links from a given HTML line
    private static List<String> extractWikiLinks(String line) {
        List<String> wikiLinks = new ArrayList<>();
        String regex = "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\"[^>]*>(.*?)</a>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String link = matcher.group(1);
            if (isValidWikiLink(link)) {
                wikiLinks.add(link);
            }
        }
        return wikiLinks;
    }
}
