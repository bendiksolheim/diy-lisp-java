# Erfaringer Diy-lisp-java
- Må fikse litt opp i Parser.java:
    - Endre rekkefølgen på ting slik at de funksjonene man faktisk skal bruke ligger øverst
    - Lage kommentarer på funksjonene
- toString må gi mer verdifull informasjon, og ikke brukes til unparsing
- REPL: prøver å definere funksjonen `not`, men man har ikke implementert `define`. Ting tryner.
    - REPLet forventer også å finne et uttrykk i stdlib.diy. Løsning: skal ikke forvente dette
- muligens fjern quote-funksjonen i SList