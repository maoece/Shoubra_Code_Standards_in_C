import org.json.simple.JSONObject;
public class overwriteVisitor extends CBaseVisitor<String[]> {

    String Filename ;
    JSONObject declareObject;
    JSONObject globalObject;
    JSONObject localObject;

    overwriteVisitor(JSONObject declareObject, JSONObject globalObject, JSONObject localObject, String m){
        this.declareObject = declareObject;
        this.globalObject = globalObject;
        this.localObject = localObject;
        this.Filename = m ;
    }

    @Override public String[] visitGlobalDeclaration(CParser.GlobalDeclarationContext ctx) {
        //System.out.println("Global declaration " + ctx.getText() + " in line: " + ctx.start.getLine());
        CParser.DeclarationSpecifiersContext declarationSpecifiers = ctx.declaration().declarationSpecifiers();
        int numOfchilds = declarationSpecifiers.getChildCount();
        //Global variable
        if(numOfchilds == 2){
            String type = declarationSpecifiers.declarationSpecifier(0).getText();
            String id = declarationSpecifiers.declarationSpecifier(1).getText();

            if( type.length() > 4 &&type.substring(0,4).equals("enum") && !id.matches((String) globalObject.get("enum"))){
                System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + id + "\" Should Be In This Format: " + (String) globalObject.get("enum") + " ]");
            } else if(type.length() > 5 && type.substring(0,5).equals("union") && !id.matches((String) globalObject.get("union"))){
                System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + id + "\" Should Be In This Format: " + (String) globalObject.get("union") + " ]");
            } else if(type.length() > 6 && type.substring(0,6).equals("struct") && !id.matches((String) globalObject.get("struct"))){
                System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + id + "\" Should Be In This Format: " + (String) globalObject.get("struct") + " ]");
            } else if(!id.matches((String) globalObject.get("other vars"))){
                System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + id + "\" Should Be In This Format: " + (String) globalObject.get("other vars") + " ]");
            }
            //System.out.println("Global, type: " + type + ", id: " + id);
        }
        //declaration of struct or union or enum
        else if(numOfchilds == 1){
            String[] typeAndID = visit(declarationSpecifiers.declarationSpecifier(0).typeSpecifier());
            //System.out.println("Data structure, type: " + typeAndID[0] + ", id: " + typeAndID[1]);

            if(!typeAndID[1].matches((String) declareObject.get(typeAndID[0]))){
                System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + typeAndID[1] + "\" Should Be In This Format: " + (String) declareObject.get(typeAndID[0]) + " ]");
            }
        }
        String[] s = {ctx.getText()};
        return s;
    }

    @Override public String[] visitLocalDeclaration(CParser.LocalDeclarationContext ctx) {
        //System.out.println("Local declaration " + ctx.getText() + " in line: " + ctx.start.getLine());
        CParser.DeclarationSpecifiersContext declarationSpecifiers = ctx.declaration().declarationSpecifiers();
        int numOfchilds = declarationSpecifiers.getChildCount();

        if (numOfchilds == 2) {
            String type = declarationSpecifiers.declarationSpecifier(0).getText();
            String id = declarationSpecifiers.declarationSpecifier(1).getText();

            if (type.length() > 4 && type.substring(0,4).equals("enum") && !id.matches((String) localObject.get("enum"))) {
                System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + id + "\" Should Be In This Format: " + (String) localObject.get("enum") + " ]");
            } else if (type.length() > 5 && type.substring(0,5).equals("union") && !id.matches((String) localObject.get("union"))) {
                System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + id + "\" Should Be In This Format: " + (String) localObject.get("union") + " ]");
            } else if (type.length() > 6 && type.substring(0,6).equals("struct") && !id.matches((String) localObject.get("struct"))) {
                System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + id + "\" Should Be In This Format: " + (String) localObject.get("struct") + " ]");
            } else if (!id.matches((String) localObject.get("other vars"))) {
                System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + id + "\" Should Be In This Format: " + (String) localObject.get("other vars") + " ]");
            }
        }
        //System.out.println("local, type: " + type + ", id: " + id);
        else if (numOfchilds == 1) {
            String[] typeAndID = visit(declarationSpecifiers.declarationSpecifier(0).typeSpecifier());
            //System.out.println("Data structure, type: " + typeAndID[0] + ", id: " + typeAndID[1]);

            if (!typeAndID[1].matches((String) declareObject.get(typeAndID[0]))) {
                System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + typeAndID[1] + "\" Should Be In This Format: " + (String) declareObject.get(typeAndID[0]) + " ]");
            }
        }

        String[] s = {ctx.getText()};
        return s;
    }

    @Override public String[] visitTypeSpecifierStructOrUnion(CParser.TypeSpecifierStructOrUnionContext ctx) {
        String type = ctx.structOrUnionSpecifier().structOrUnion().getText();
        String id = ctx.structOrUnionSpecifier().Identifier().getText();
        String[] typeAndName = {type,id};
        return typeAndName;
    }

    @Override public String[] visitTypeSpecifierEnum(CParser.TypeSpecifierEnumContext ctx) {

        String type = ctx.enumSpecifier().Enum().getText();
        String id = ctx.enumSpecifier().Identifier().getText();
        String[] typeAndName = {type,id};
        return typeAndName;
    }

    //Function
    @Override public String[] visitExternalFunctionDefinition(CParser.ExternalFunctionDefinitionContext ctx) {
        String type = ctx.functionDefinition().declarationSpecifiers().declarationSpecifier(0).getText();
        String id = ctx.functionDefinition().declarator().directDeclarator().directDeclarator().getText();
        //System.out.println("function, return type: "+ type + ", id:" + id);
        if(!id.matches((String) declareObject.get("function"))){
            System.out.println("Error in: \"" + Filename + "\" File & Line: # " + ctx.start.getLine() + " => [ \"" + id + "\" Should Be In This Format: " + (String) declareObject.get("function") + " ]");
        }
        visitChildren(ctx);
        String[] s = {ctx.getText()};
        return s;
    }

    //Parameter Function
    @Override public String[] visitParameterDeclaration(CParser.ParameterDeclarationContext ctx) {
        String type = ctx.declarationSpecifiers().declarationSpecifier(0).getText();
        String id = ctx.declarator().getText();
        //System.out.println("Function parameter, type: " + type + ", id:" + id);
        String[] s = {ctx.getText()};
        return s;
    }

}
