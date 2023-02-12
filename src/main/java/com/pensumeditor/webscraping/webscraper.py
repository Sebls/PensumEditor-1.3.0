import requests
import re
from bs4 import BeautifulSoup

URL = "https://programasacademicos.unal.edu.co/programa/pre250-ingenieria-mecatronica"
fileName = "ing_mecatronica_pensum.pns"
name = "Ingeniería Mecatrónica"
visalization = "ING"

def getSubjects(results, component):
    blocks = results.find_all("div", class_="repos-bloque")

    for block in blocks:
        group = block.find("div", class_="repos-bloque-nombre").getText()
        
        subjects = block.find_all("div", class_=re.compile("^repos-asignatura repos-"))
        for subject in subjects:
            #print(component)
            #print("Grupo: " + group)
            name = subject.find("div", class_="repos-asignatura-nombre").getText()
            credits = subject.find("div", class_="repos-asignatura-creditos").getText()
            code = subject.find("div", class_="repos-asignatura-id").getText().replace("-B", "")

            #print("Nombre: " + name)
            #print("Creditos: " + credits)
            #print("Codigo: " + code)

            prerequisites = subject.find("div", class_="repos-asignatura-prerrequisitos").find_all("a", class_="repos-asignatura-link data")

            #print("Prerequisitos: ")
            prerequisites_string = ""
            for prerequisite in prerequisites:
                #print("  - " +  prerequisite.getText())
                prerequisites_string += prerequisite.getText() + " - "
            if component == "COMPONENTE DE LIBRE ELECCIÓN":
                print(f'Subjects.add( new Subject({code}, "{name}", {credits[0]}, "Libre elección", "{prerequisites_string}", "{component}") );')
            else:
                print(f'Subjects.add( new Subject({code}, "{name}", {credits[0]}, "{group}", "{prerequisites_string}", "{component}") );')

def scrap(URL, fileName, name, visalization):
    page = requests.get(URL)

    soup = BeautifulSoup(page.content, "html.parser")

    print("ArrayList<Subject> Subjects = new ArrayList<>();")
    print()

    results = soup.find("div", class_="repos-arco componente-de-fundamentacion")
    getSubjects(results, "COMPONENTE DE FUNDAMENTACIÓN")

    results = soup.find("div", class_="repos-arco componente-de-formacion-disciplinar-o-profesional")
    getSubjects(results, "COMPONENTE DE FORMACIÓN DISCIPLINAR O PROFESIONAL")

    results = soup.find("div", class_="repos-arco trabajo-de-grado")
    getSubjects(results, "COMPONENTE DE FORMACIÓN DISCIPLINAR O PROFESIONAL")

    results = soup.find("div", class_="repos-arco d-none componente-de-libre-eleccion")
    getSubjects(results, "COMPONENTE DE LIBRE ELECCIÓN")

    print()
    print(f'Serialization.ObjectSerialization("src/main/resources/com/pensumeditor/projects/{fileName}", new Pensum("{name}", "{visalization}", Subjects));')

    print()
    print(f'mainController.loadProject(Serialization.ObjectDeserialization("src/main/resources/com/pensumeditor/projects/{fileName}", Pensum.class));')

scrap(URL, fileName, name, visalization)