int Global_time;
float Global_size;
char Global_width;

struct Declare_struct1
{
   str_var1;
   str_var2;
};

enum Declare_enum1
{
   enum1_var1,
   enum1_var2,
   enum1_var3,
};

union Declare_union1
{
   int first;
   float second;
   char name[20];
};

enum Declare_enum1 Global_enum2;
struct Declare_struct1 Global_struct2;
union Declare_union1 Global_union2;

int Declare_main(int raw , int coloumn )
{
    char Local_character;
    struct Declare_struct3
    {
       int   str2_var1;
       char  str2_var2;
       float str2_var3;

       enum Declare_enum3
          {
              enum1_var1,
              enum1_var2,
          };
    };

    struct Declare_struct3 Local_1;
}