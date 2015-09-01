package models;

public class Response {
  
  final Answer response;
  final Integer responseTime;
  
  public Response(Answer response, Integer responseTime) {
    this.response = response;
    this.responseTime = responseTime;
  }
  
//  @Override
//  public String toString() {
//    return String.format("{response: %s, correctResponse: %s, responseTime: %d}", 
//        this.response.toString(), this.correctResponse.toString(), this.responseTime);
//  }
//  
//  
//  
//  
//  public static ObjectParser<Response> getParser() {
//    return new ObjectParser<Response>() {
//      private String 
//      
//      @Override
//      public Response parseFromString(String str) {
//        str = str.trim();
//        static String RESPONSE_REGEX = "\\{.*response[ ]*:[ ]*([^,]*).*\\}";
//        Pattern p = Pattern.compile(RESPONSE_REGEX);
//        Matcher m = p.matcher(str);
//        System.out.printf("Matched group count: %d\n", m.groupCount());
//        if (m.find()) {
//          for (int i = 0; i < m.groupCount() + 1; i++) {
//            System.out.printf("Match %d:|%s|\n", i, m.group(i));
//          }
//        }
//        return null;
//      }
//    };
//  }
//  
//  public static void main(String[] args) {
//    String tests[] = new String[] {
//      "{ response : A , correctResponse : B , responseTime : 123 }"
//    };
//    
//    ObjectParser<Response> op = getParser();
//    
//    for (String test : tests) {
//      System.out.printf("TESTING STRING: |%s|\n", test);
//      op.parseFromString(test);
//    }
//    
//  }
  
  
}
