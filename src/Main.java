import java.util.*;

public class Main {
    private static int[][][][] adjacency = {
            {{{0, 1}, {1, 0}}, {{0, 0}, {0, 2}, {1, 1}}, {{0, 1}, {1, 2}}},//row 0
            {{{0, 0}, {1, 1}, {2, 0}}, {{0, 1}, {1, 0}, {1, 2}, {2, 1}}, {{0, 2}, {1, 1}, {2, 2}}},//row 1
            {{{1, 0}, {2, 1}}, {{1, 1}, {2, 0}, {2, 2}}, {{1, 2}, {2, 1}}} //row 2
    };
    private static int steps = 0;

    private static final int[][] solved = {{1,2,3},{4,5,6},{7,8,0}};

    private static String empty;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to the Sliding Puzzle Solver! \n\nThis Program will solve your 8-number sliding puzzle.");
        System.out.println("The 8 puzzle problem consists of a 3 by 3 board with eight different numbered tiles (1 to 8) and an empty slot. \nThe player must move the vacant space around to arrange the puzzle until it looks like the table below: \n");
        printBoard(solved);
        System.out.println("\nPress RETURN to continue.");
        empty = scan.nextLine();
        clearScreen();
        new_board();

    }

    public static void new_board() {
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Please input your puzzle board with a space in between every number. For the vacant space, enter 0 instead.\n");
        System.out.println("For example, input 1 2 3 4 5 6 7 8 0 if your board looks like:");
        printBoard(new int[][] {{1,2,3},{4,5,6},{7,8,0}});

        String init1 = myScanner.nextLine();
        ArrayList<Character> options = new ArrayList<>(List.of('1','2','3','4','5','6','7','8','9','0'));
        if(init1.length()!=17){
            System.out.println("Invalid input. Wrong length. Try again by pressing RETURN.");
            invalid();
            return;
        }
        for (int i = 1; i < 17; i+=2){
            if(init1.charAt(i) != ' '){
                System.out.println("Invalid input. Space Issues. Try again by pressing RETURN.");
                invalid();
                return;
            }
        }

        for (int i = 0; options.size()>0 && i<18; i+=2){
            if(!(options.contains(init1.charAt(i)))){
                System.out.println("Invalid input. Repeated/invalid numbers. Try again by pressing RETURN.");
                invalid();
                return;
            }else{
                options.remove(Character.valueOf(init1.charAt(i)));
            }
        }

        String[] init2 = init1.split(" ");
        int[] init3 = new int[9];
        for (int i =0; i<9; i++){
            init3[i] = Integer.valueOf(init2[i]);
        }

        solvable(init3);



    }

    public static void solvable(int[] init){
        int inversions = 0;
        for (int i = 0; i < 9; i++){
            for (int j = i+1; j<9; j++){
                if (init[i] > init[j] && init[i]!= 0 && init[j]!=0)
                    inversions++;
            }
        }

        if (inversions%2!=0){
            System.out.println("The puzzle board is unsolvable. Press RETURN to enter a new puzzle board.");
            invalid();
            return;
        }else{
            System.out.println("This is solvable. Please wait for the program to load...");
        }

        int[][] init4 = new int[3][3];
        for (int i = 0; i<3; i++){
            for (int j = 0; j < 3; j++)
                init4[i][j] = init[i*3 + j];
        }
//        for(int[] t: init4)
//            System.out.println(Arrays.toString(t));
        solve(init4);

    }


    public static void solve(int[][] init){
        ArrayList<int[][]> final_solution = new ArrayList<>();

        if(Arrays.equals(init[0],solved[0]) && Arrays.equals(init[1],solved[1]) && Arrays.equals(init[2],solved[2])){
            final_solution.add(init);
        }else
            final_solution = BFS(init);
        display(final_solution);
    }

    public static void display(ArrayList<int[][]> final_solution) {
        Scanner myScanner = new Scanner(System.in);
        for (int i = final_solution.size() - 1; i >= 0; i--) {
            clearScreen();

            if (i == final_solution.size() - 1)
                System.out.println("This is what you've inputted.");
            else
                System.out.println("Step " + (final_solution.size() - i - 1) + " out of " + (final_solution.size() - 1));


            printBoard(final_solution.get(i));

            if (i != 0) {
                System.out.println("Enter RETURN to see the next step.");
                empty = myScanner.nextLine();
            } else {
                System.out.println("Congratulations. You are at the end of the solution. \nEnter RETURN to choose what you want to do next.");
                empty = myScanner.nextLine();
            }

        }

        int next = 0;
        boolean first = true;
        while (next < 1 || next > 3) {
            clearScreen();
            if (first == false)
                System.out.println("Invalid input. Try again.");
            end();
            next = myScanner.nextInt();
            first = false;
        }
        if (next == 1)
            display(final_solution);
        else if (next == 2){
            final_solution.clear();
            new_board();
        }else{
            System.out.println("Thank you for using this program!");
            System.exit(0);
        }


    }

    public static void end(){
        System.out.println("Enter the number corresponding to your next action.");
        System.out.println("+————+—————————————————————————————————+");
        System.out.println("| #  | Next Step                       |");
        System.out.println("+————+—————————————————————————————————+");
        System.out.println("| 1  | Look through the solution again |");
        System.out.println("+————+—————————————————————————————————+");
        System.out.println("| 2  | Solve a new puzzle              |");
        System.out.println("+————+—————————————————————————————————+");
        System.out.println("| 3  | Quit                            |");
        System.out.println("+————+—————————————————————————————————+");

    }

    public static ArrayList<int[][]> BFS(int[][] init) {
        ArrayList<int[][]> output = new ArrayList<>();
        ArrayList<ArrayList<int[][]>> seen1 = new ArrayList<>();
        ArrayList<ArrayList<int[][]>> seen2 = new ArrayList<>(); //list of steps
        ArrayList<int[]> queue1 = new ArrayList<>();
        ArrayList<int[][]> queue2 = new ArrayList<>();
        steps = 0;
        output.addAll(Collections.singleton(init));
        seen2.addAll(Collections.singleton(output));

        while(steps < 32){
            steps++;
            //System.out.println(steps);
            for (ArrayList<int[][]> current_steps: seen2){
                int[][] current = current_steps.get(0);
                for (int[] i : adjacency[linear_search(current)[0]][linear_search(current)[1]])
                    queue1.addAll(Collections.singleton(i));

                for (int[] new_pos : queue1) {
                    int[][] temp = change_board(current, new_pos, linear_search(current));
                    queue2.addAll(0, Collections.singleton(temp));
                }

                boolean repeat = false;

//                System.out.println("queue2:");
                for (int[][] i : queue2){
                    //print_board(i);
                    repeat = false;
                    for (int[][] j: current_steps){
//                        System.out.println("Current steps is:");
//                        print_board(j);
                        if (Arrays.equals(i[0], j[0]) && Arrays.equals(i[1], j[1]) && Arrays.equals(i[2], j[2])){
//                            System.out.println("i is the same!");
//                            print_board(i);
                            //queue2.removeAll(Collections.singleton(i));
                            repeat = true;
                        }

                    }
                    if (repeat == false){
                        current_steps.add(0, i); //works

                        ArrayList<int[][]> tempor = new ArrayList<>();
                        for (int[][] t: current_steps)
                            tempor.add(tempor.size(), t);

                        seen1.add(0,tempor);
                        //tempor.clear();
//                        System.out.println("Steps:");
//                        for (ArrayList<int[][]> p: seen1){
//                            for (int[][] k: p){
//                                print_board(k);
//                            }
//                        }
                        //System.out.println("Seen 1 Done");

                        current_steps.remove(0); //works
                    }

                }
//                for (ArrayList<int[][]> j: seen1){
//                    for (int[][] k: j){
//                        print_board(k);
//                    }
//                }
                queue1.clear();
                queue2.clear();


            }
            seen2.clear();

            for (ArrayList<int[][]> current: seen1){
                if (Arrays.equals(current.get(0)[0],solved[0]) && Arrays.equals(current.get(0)[1],solved[1]) && Arrays.equals(current.get(0)[2],solved[2])){
                    return current;
                }else{
                    seen2.addAll(Collections.singleton(current));
                }

            }
//            System.out.println("seen2:");
//            for (ArrayList<int[][]> j: seen2){
//                System.out.println("Steps:");
//                for (int[][] k: j){
//                    printBoard(k);
//                }
//            }
            seen1.clear();

        }

        return null;
    }

    public static void invalid(){
        Scanner myScanner = new Scanner(System.in);
        empty = myScanner.nextLine();
        clearScreen();
        new_board();
    }


    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printBoard(int[][] board){
        String[][] output = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                if (board[i][j] != 0)
                    output[i][j] = String.valueOf(board[i][j]);
                else
                    output[i][j] = " ";
            }
        }
        System.out.println("+————+————+————+");
        System.out.println("｜ "+output[0][0]+" ｜ "+output[0][1]+" ｜ "+output[0][2]+" ｜");
        System.out.println("+————+————+————+");
        System.out.println("｜ "+output[1][0]+" ｜ "+output[1][1]+" ｜ "+output[1][2]+" ｜");
        System.out.println("+————+————+————+");
        System.out.println("｜ "+output[2][0]+" ｜ "+output[2][1]+" ｜ "+output[2][2]+" ｜");
        System.out.println("+————+————+————+");
    }

    public static int[] linear_search(int[][]curr){
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                if (curr[i][j] == 0)
                    return (new int[] {i,j});

            }
        }
        return new int[] {0};
    }

    public static int[][] change_board(int[][] curr, int[] new_pos, int[] old_pos){
        int[][] output = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                output[i][j] = curr[i][j];
            }
        }

        int temp = output[new_pos[0]][new_pos[1]];
        output[new_pos[0]][new_pos[1]] = 0; //works
        output[old_pos[0]][old_pos[1]] = temp;

        return output;
    }
}