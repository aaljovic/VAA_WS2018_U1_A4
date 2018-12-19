# VAA2018
Verteilte Algorithmen und Anwendungen Wintersemester 2018 Übungen


Übung 3 und 4


Voraussetzung

Das Programm wurde nur unter Betriebssystem Windows 10 getestet.
Im Programm wird JSON genutzt, classpath wird in der Script-Datei gesetzt.


Kurzbeschreibung

Das Programm ist eine Erweiterung zu den vorherigen Programmen aus Übung 1 und Übung 2.
Übungen 3 und 4 wurden in einem Projekt zusammengefasst.
Wie in den vorherigen Übungen liest das Programm die Anzahl der zu erstellenden Knoten, IP-Adresse, Port aus inputFiles/inputTextFile.
Nachbarn und Verbindungen werden vom Programm erstellt (Datei inputFiles/inputGraphFile wird überschrieben), abhängig von den Eingaben (Anzahl an Knoten und Kanten).
Weitere Funktionalitäten des Programms stehen in der Aufgabenstellung.


Ausführung

Um das Programm zu starten muss die Skript-Datei startInitiator aus dem Ordner src gestartet werden.
Ein cmd-Fenster wird geöffnet. Geben Sie die Anzahl der Knoten und Kanten ein.
Wenn erfolgreich, führen Sie das Script startNodes aus dem Ordner src.
Geben Sie die Anzahl an Knoten ein. Das Script startet mehrere cmd-Fenster.
Weitere Schritte werden vom Initiator angezeigt.


Schwächen

- die Eingabe-Dateien (inputTextFile und inputGraphFile) sind im Quellcode festgelegt (werden nicht als Parameter übergeben)
- die Ausgabe ist teilweise unübersichtlich
- c wird nicht als Parameter mitgegeben. Nach dem beenden eines Knoten zeigt dieser die Anzahl an gehörten Gerüchten
- Nur ein Nachrichtenformat für Geheime Nachricht und Kontroll Nachricht


Stärken

- Rest der Anforderung sind erfüllt


Informationen

- JSON Nachrichtenformat
- Java Programmiersprache