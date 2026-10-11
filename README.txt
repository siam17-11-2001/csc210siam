Lab 04 - Graphical User Interfaces

This package is prepared for the existing repository:
https://github.com/siam17-11-2001/csc210siam

Copy the enclosed lab-04 folder into the root of your existing local repository.
The Creature.java fields and methods match the original lab-03 source.
ProcessCreatureFile.java has been expanded to provide CSV-backed CRUD methods.

TO COMPILE (open the terminal in the lab-04 folder):
    javac *.java

TO RUN (one at a time):
    java HelloGUI
    java SingleCreatureGUI
    java MultipleCreatureGUI
    java CreaturesFromFileGUI

IMPORTANT:
- Launch CreaturesFromFileGUI from within lab-04 so it finds creature-data.csv.
- The Save button updates the selected object in memory.
- The Save to File button writes changes to creature-data.csv.
- Open File lets you choose another CSV. Each CSV row must contain name,size,age.
- The old command-line demonstration is available: java Creature
  and java ProcessCreatureFile. The latter modifies creature-data.csv.

SUBMIT:
Upload lab-04 to the same GitHub repository. Add the professor as a
collaborator (GitHub: matthewscottconroy) if not already. Email
msconroy@bmcc.cuny.edu with subject "Lab 04 Graphical User Interfaces".
Include the repository URL and "Hours spent: ___" with an honest estimate.
