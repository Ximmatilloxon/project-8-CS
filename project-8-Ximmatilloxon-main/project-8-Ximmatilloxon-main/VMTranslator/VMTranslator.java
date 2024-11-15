import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Translate virtual machine code (.vm) to assembly code (.asm).
class VMTranslator {


  // SP--, D = *SP, SP--, *SP = D + *SP
  private static final String ADD = "";

  // SP--, D = *SP, SP--, *SP = *SP - D
  private static final String SUB ="";

  // SP--, *SP = -*SP
  private static final String NEG = "";

  // SP--, D = *SP, SP--, *SP = D & *SP
  private static final String AND = "";

  // SP--, D = *SP, SP--, *SP = D | *SP
  private static final String OR ="";

  // SP--, *SP = !*SP
  private static final String NOT = "";

  // *SP = D, SP++
  private static final String PUSH ="";

  // S--, D = *SP
  private static final String POP ="";


  // R13 = *(LCL - 5)
  // *ARG = *(SP - 1)
  // SP = ARG + 1
  // THAT = *(LCL - 1); LCL--
  // THIS = *(LCL - 1); LCL--
  // ARG = *(LCL - 1); LCL--
  // LCL = *(LCL - 1)
  // A = R13, jump
  private static final String RETURN ="";

  private static int count = 0;

  private BufferedReader br;
  private String[] files;
  private int fileIdx = 0;
  private String currFileName;
  private String funcName;

  public VMTranslator(String[] files) {
    for (String file : files) {
      System.err.println("file: " + file);
    }
    this.files = files;
    return;
  }

  private String nextCount() {
    count += 1;
    return Integer.toString(count);
  }

  private String nextCommand() throws IOException {
    if (br == null)
      if (!open())
        return null;
    String line;
    while(true) {
      line = br.readLine();
      if (line == null) {
        close();
        br = null;
        return nextCommand();
      }
      line = line.replaceAll("//.*", "").trim();
      if (line.length() == 0)
        continue;
      return line;
    }
  }

  private String parseNextCommand() throws Exception {
    String s = nextCommand();
    System.out.println("//" + s);
    if (s == null)
      return null;
    switch (s) {
      case "add": {
        return ADD;
      }
      case "sub": {
        return SUB;
      }
      case "neg": {
        return NEG;
      }
      case "eq": {
        return EQ();
      }
      case "gt": {
        return GT();
      }
      case "lt": {
        return LT();
      }
      case "and": {
        return AND;
      }
      case "or": {
        return OR;
      }
      case "not": {
        return NOT;
      }
      default: {
        String[] parts = s.split(" ");
        if (parts.length == 0)
          throw new Exception("bad command!");
        switch (parts[0]) {
          case "push": return parsePush(parts[1], parts[2]);
          case "pop": return parsePop(parts[1], parts[2]);
          case "label": return "(" + funcName + "$" + parts[1] + ")\n";
          case "goto": return GOTO(parts[1]);
          case "if-goto": return IFGOTO(parts[1]);
          case "function": { funcName = parts[1]; return FUNCTION(parts[1], parts[2]); }
          case "call": return CALL(parts[1], parts[2]);
          case "return": { return RETURN; }
          default: throw new Exception("bad command! " + parts[0]);
        }
      }
    }
  }

  private String parsePop(String base, String idx) throws Exception {
    switch (base) {
      case "local": {
        return "";
        // R13 = LCL + idx, pop to D, *R13 = D
      }
      case "argument": {
        return "";
        // R13 = ARG + idx, pop to D, *R13 = D
      }
      case "this": {
        return "";
        // R13 = THIS + idx, pop to D, *R13 = D
      }
      case "that": {
        return "";
        // R13 = THAT + idx, pop to D, *R13 = D
      }
      case "pointer": {
        if (idx.equals("0"))
          return "";
          // pop to D, THIS = D
        else
          return "";
          // pop to D, THAT = D
      }
      case "static": {
        return "";
          // pop to D, currFileName.idx = D
      }
      case "temp": {
        return "";
        // R13 = R5 + idx, pop to D, *R13 = D
      }
      default: throw new Exception("bad command!");
    }
  }

  private String parsePush(String base, String idx) throws Exception {
    switch (base) {
      case "local": {
        return "";
        // D = *(LCL + idx), push D
      }
      case "argument": {
        return "";
        // D = *(ARG + idx), push D
      }
      case "this": {
        return "";
        // D = *(THIS + idx), push D
      }
      case "that": {
        return "";
        // D = *(THAT + idx), push D
      }
      case "pointer": {
        if (idx.equals("0"))
          return "";
        // D = THIS, push D
        else
          return "";
        // D = THAT, push D
      }
      case "constant": {
        return "";
        // D = constant, push D
      }
      case "static": {
        return "";
        // D = curFileName.idx, push D
      }
      case "temp": {
        return "";
        // D = (R5+idx), push D
      }
      default: throw new Exception("bad command!");
    }
  }

  private boolean open() {
    try {
      if (br == null && fileIdx != files.length) {
        br = new BufferedReader(new FileReader(files[fileIdx]));
        currFileName = files[fileIdx].replaceAll(".*/", "");
        fileIdx += 1;
        return true;
      } else
        return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private void close() {
    try {
      if (br != null)
        br.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private String EQ() {
    // use nextCount() to get a unique label
    String n = nextCount();

    // SP--, D = *SP, SP--, D = D - *SP, if D == 0, *SP = -1, else *SP = 0
    String s = "";
    return s;
  }

  private String GT() {
    // use nextCount() to get a unique label
    String n = nextCount();
    // SP--, D = *SP, SP--, D = D - *SP, if D > 0, *SP = -1, else *SP = 0
    String s = "";
    return s;
  }

  private String LT() {
    // use nextCount() to get a unique label
    String n = nextCount();
    // SP--, D = *SP, SP--, D = D - *SP, if D < 0, *SP = -1, else *SP = 0
    String s = "";
    return s;
  }

  private String GOTO(String label) {
    String s ="";
    // jump to @funcName$label
    return s;
  }

  private String IFGOTO(String label) {
    String s = "";
    // SP--, D = *SP, if D != 0, jump to @funcName$label
    return s;
  }

  private String FUNCTION(String f, String k) {
    // create label (f), repeat k times: push 0
    String s = "";
    return s;
  }

  private String CALL(String f, String n) {
    String c = nextCount();
    String s = "";
    // R13 = SP
    // *SP = @RET.c; SP++
    // *SP = LCL; SP++
    // *SP = ARG; SP++
    // *SP = THIS; SP++
    // *SP = THAT; SP++
    // ARG = R13 - 5
    // LCL = SP
    // select f, jump
    // (RET.c)

    return s;

  }

  public static void main(String files[]) {
    if (files.length == 0) {
      System.err.println("no input files");
      return;
    }
    VMTranslator p = new VMTranslator(files);
    String init =
      "@256\n" +
      "D=A\n" +
      "@SP\n" +
      "M=D\n" +
      "// call Sys.init 0\n" +
      p.CALL("Sys.init", "0") +
      "0;JMP\n";
    System.out.println(init);
    String s;
    try {
      while(true) {
        s = p.parseNextCommand();
        if (s == null)
          return;
        System.out.println(s);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
