package com.pensumeditor.webscraping;
import com.pensumeditor.data.AcademicProgram;
import com.pensumeditor.data.Subject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubjectScraper {
    private final Pattern pattern = Pattern.compile("\\d+");
    Set<String> groups;
    public HashSet<Subject> webScraping(String url) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        HashSet<Subject> SubjectSet = new LinkedHashSet<>();
        this.groups = new LinkedHashSet<>();

        certificate();
        Document document = Jsoup.connect(url).get();

        Elements components = document.select("*[class~=^repos-arco.*]");

        for (Element component : components) {
            String componentName = component.select("h3").text();
            Elements blocks = component.getElementsByClass("repos-bloque");
            for (Element block : blocks) {
                String group = block.getElementsByClass("repos-bloque-nombre").first().text().replace(" (SB)", "");
                if (componentName.toLowerCase().contains("libre eleccion") || componentName.toLowerCase().contains("libre elección")) {
                    group = "Libre elección";
                }
                if (!groups.contains(group) && !group.toLowerCase().contains("arco vacio") && !group.toLowerCase().contains("trabajo de grado")) {
                    groups.add(group);
                }
                Elements subjects = block.select("*[class~=^repos-asignatura repos-.*]");
                for (Element subject : subjects) {
                    String name = subject.getElementsByClass("repos-asignatura-nombre").first().text();
                    int credits = Integer.valueOf(subject.getElementsByClass("repos-asignatura-creditos").first().text().replace("c", ""));
                    Matcher matcher = pattern.matcher(subject.getElementsByClass("repos-asignatura-id").first().text());
                    int code = 1000000;
                    if (matcher.find()) {
                        code = Integer.valueOf(matcher.group());
                    }
                    String prerequisites = "";
                    for (Element prerequisite : subject.getElementsByClass("repos-asignatura-link data")) {
                        if (!prerequisites.equals("")) {
                            prerequisites = prerequisites + ", " + prerequisite.text();
                            continue;
                        }
                        prerequisites = prerequisite.text();
                    }
                    SubjectSet.add(new Subject(code, name, credits, group, prerequisites, componentName));
                }
            }
        }
        SubjectSet.add(new Subject(1000000, "Electiva", 2, "Libre elección", "", "Componente de Libre Elección"));
        SubjectSet.add(new Subject(1000000, "Electiva", 3, "Libre elección", "", "Componente de Libre Elección"));
        SubjectSet.add(new Subject(1000000, "Electiva", 4, "Libre elección", "", "Componente de Libre Elección"));
        this.groups.add("Trabajo de Grado");
        return SubjectSet;
    }

    public ArrayList<AcademicProgram> getAcademicPrograms() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        ArrayList<AcademicProgram> academicPrograms = new ArrayList<>();
        certificate();
        Document document = Jsoup.connect("https://programasacademicos.unal.edu.co/buscar").get();
        Elements sedes = document.getElementsByClass("group-campus card mb-0");
        for (Element sede : sedes) {
            String Location = sede.select("h4").first().text();
            Elements academicProgram = sede.getElementsByClass("btn btn-secondary btn-block text-left text-white");
            for (Element element : academicProgram) {
                String URL = element.attr("href");
                String Name = URL.replace("/programa/", "").split("-", 2)[1].replace("-", " ");
                Name = Name.substring(0, 1).toUpperCase() + Name.substring(1);
                String Type = URL.replace("/programa/", "").split("-", 2)[0];
                String Code = element.text();
                if (Name.equals("Filologia e idiomas")) {
                    switch (Integer.valueOf(Code)) {
                        case 2524:
                            Name = "Filología e idiomas - Alemán";
                            break;
                        case 2525:
                            Name = "Filología e idiomas - Francés";
                            break;
                        default:
                            Name = "Filología e idiomas - Inglés";
                            break;
                    }
                }
                if (Type.contains("pre")) {
                    Type = "Pregrado";
                } else if (Type.contains("pos")) {
                    Type = "Posgrado";
                }
                academicPrograms.add(new AcademicProgram(Code, Name, Type, Location, URL));
            }
        }
        return academicPrograms;
    }

    public void certificate() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    public Set<String> getGroups() {
        return groups;
    }
}

