# FITS Header Editor

This is engine part of **FITS Header Editor** application executable from *command line*. It can be used as a part of graphical user interface, found in standalone repository – [FITS Header Visual Editor](https://github.com/supermartzin/fits-header-visual-editor)

-----
_Following description is in slovak and czech language:_

**Program na hromadnú úpravu hlavičiek FITS súborov.**

Projekt vznikol ako bakalárska práca na **Fakulte informatiky** Masarykovej univerzity v Brne v roku 2015.

**Vedúci práce**: [RNDr. Martin Kuba, Ph.D.](https://is.muni.cz/osoba/3988)

*Konzultant*: [doc. RNDr. Miloslav Zejda, Ph.D.](https://is.muni.cz/osoba/169695)


## Oficiálne zadanie: ##
Formát **FITS** (*Flexible Image Transport System*) je formát používaný pro ukládání astronomických dat. Soubor v tomto formátu obsahuje jednu nebo více tzv. **HDU** (*header data unit*), každá HDU má hlavičku a data. Hlavička obsahuje hlavičkové záznamy (*card image*), kde každý z nich obsahuje klíčové slovo (*keyword*), hodnotu a komentář.

Cílem práce je pro ***Ústav teoretické fyziky a astrofyziky PřF MU*** vytvořit nástroj pro hromadné úpravy hlavičkových záznamů ve FITS souborech. Tento nástroj by měl být tvořen dvěma částmi:

* programem spustitelným z příkazového řádku nebo skriptu, který provede zadané změny na všech zadaných souborech
* grafickým rozhraním pro pohodlný výběr souborů a zadání úprav hlaviček

Grafické rozhraní umožní vybrat soubory následujícím postupem:

* vybrat adresář
* v adresáři vyfiltrovat soubory podle názvu s využitím tzv. **globbingu** (*znaky * a ?*)
* z vyfiltrovaných souborů vybrat buď všechny, nebo jen některé pomocí myši
* volitelně v dalším kroku z nich vybrat jen ty soubory, které mají v hlavičkovém záznamu se zadaným klíčovým slovem zadanou hodnotu

Grafické rozhraní umožní specifikovat a program bude umět provést následující změny hlavičkových záznamů:

* **odebrat** hlavičkový záznam se zadaným klíčovým slovem
* **přidat** nový hlavičkový záznam, tedy klíčové slovo, hodnotu a komentář
* **vyměnit** zadané klíčové slovo za jiné zadané
* pro zadané klíčové slovo vyměnit hodnotu buď za jinou pevně stanovenou hodnotu, nebo v případě časové hodnoty posunout čas o zadaný posun
* **provést zřetězení** hodnot libovolného počtu hlavičkových záznamů a zadaných konstant do jednoho hlavičkového záznamu (*může být nový, nebo přepsat hodnotu existujícího*)

Programovací jazyk zvolte tak, aby nástroj byl použitelný minimálně na platformách **MS Windows** a **Linux**.
