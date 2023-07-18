package com.company.partition;

import com.company.utils.myRandom;

import java.util.*;

/**
 * @Author Haoming Zeng
 * @Date 2023/7/16 20:25
 * @classDescribe implement Static Partition testing strategy proposed by Sabor K K, Thiel S.
 * Adaptive random testing by static partitioning[C]
 * 2015 IEEE/ACM 10th International Workshop on Automation of Software Test. IEEE, 2015: 28-32.
 */
public class StaticPartition {
    private int p; // divide the input area into p*p cells
    private HashSet<String> whiteCells;
    private HashSet<String> greenCells;
    private HashSet<String> yellowCells;
    private HashSet<String> redCells;

    public StaticPartition(int cells)
    {
        //initialize
        this.p=cells;
        yellowCells = new HashSet<>();
        redCells = new HashSet<>();
        greenCells = new HashSet<>();
        whiteCells = new HashSet<>();
        for(int i=0;i<p;i++)
        {
            for (int j=0;j<p;j++)
            {
                whiteCells.add(i+"-"+j);
            }
        }
    }

    public HashSet generator(double Xmin,double Xmax,double Ymin,double Ymax,double failureX, double failureY, double edge)
    {
        HashSet<List> executedSet = new HashSet<>();
        while(true)
        {
            List<Double> temp = new ArrayList();
            if(whiteCells.size()>0)
            {
                // randomly pick a cell from white cells
                temp = selectCaseFromCells(whiteCells,Xmin,Xmax,Ymin,Ymax);
                if(updateCells(failureX, failureY, edge, temp.get(0), temp.get(1), temp.get(2), temp.get(3), whiteCells))
                {
                    List<Double> tc = new LinkedList<>();
                    tc.add(temp.get(0));
                    tc.add(temp.get(1));
                    executedSet.add(tc);
                }
                else break;
            }
            else if(greenCells.size()>0)
            {
                // randomly pick a cell from green cells
                temp = selectCaseFromCells(greenCells,Xmin,Xmax,Ymin,Ymax);
                if(updateCells(failureX, failureY, edge, temp.get(0), temp.get(1), temp.get(2), temp.get(3), greenCells))
                {
                    List<Double> tc = new LinkedList<>();
                    tc.add(temp.get(0));
                    tc.add(temp.get(1));
                    executedSet.add(tc);
                }
                else break;
            }
            else if(yellowCells.size()>0)
            {
                // randomly pick a cell from yellow cells
                temp = selectCaseFromCells(yellowCells,Xmin,Xmax,Ymin,Ymax);
                if(updateCells(failureX, failureY, edge, temp.get(0), temp.get(1), temp.get(2), temp.get(3), yellowCells))
                {
                    List<Double> tc = new LinkedList<>();
                    tc.add(temp.get(0));
                    tc.add(temp.get(1));
                    executedSet.add(tc);
                }
                else break;
            }
            else
            {
                redCells.clear();
                for(int i=0;i<p;i++)
                {
                    for (int j=0;j<p;j++)
                    {
                        whiteCells.add(i+"-"+j);
                    }
                }
            }

        }
        return executedSet;
    }

    private List selectCaseFromCells(HashSet cellList,double Xmin,double Xmax,double Ymin,double Ymax)
    {
        // randomly pick a cell from cells
        List<String> temp = new ArrayList(cellList);
        int index = new Random().nextInt(temp.size());
        String[] selectedCell = temp.get(index).split("-");
        //calculate border of cell
        double caseXmin = Integer.valueOf(selectedCell[0])*(Xmax-Xmin)/p;
        double caseXmax = (Integer.valueOf(selectedCell[0])+1)*(Xmax-Xmin)/p;
        double caseYmin = Integer.valueOf(selectedCell[1])*(Ymax-Ymin)/p;
        double caseYmax = (Integer.valueOf(selectedCell[1])+1)*(Ymax-Ymin)/p;
        // generate test case inside the cell
        myRandom random = new myRandom();
        double tcX = random.random(caseXmin,caseXmax);
        double tcY = random.random(caseYmin,caseYmax);
        // return test case and the cell
        List<Double> ret = new LinkedList<>();
        ret.add(tcX);
        ret.add(tcY);
        ret.add(Double.valueOf(selectedCell[0]));
        ret.add(Double.valueOf(selectedCell[1]));
        return ret;
    }

    private boolean updateCells(double centerX, double centerY, double edge,double tcX, double tcY, double cellX, double cellY,HashSet cellList)
    {
        if(!((tcX>=centerX-edge/2&&tcX<=centerX+edge/2)&&(tcY>=centerY-edge/2&&tcY<=centerY+edge/2)))
        {
            String temp = "";
            temp+=(int)(cellX);
            temp+="-";
            temp+=(int)(cellY);
            cellList.remove(temp);
            redCells.add(temp);
            int xl = (int)(cellX)-1;
            int xr = (int)(cellX)+1;
            int yu = (int)(cellY)+1;
            int yd = (int)(cellY)-1;
            //deal with surrounding cells
            cellHelper(xl+"-"+yu);
            cellHelper(xr+"-"+yu);
            cellHelper(xl+"-"+yd);
            cellHelper(xr+"-"+yd);
            return true;
        }
        return false;
    }

    private void cellHelper(String cell)
    {
        if(whiteCells.contains(cell))
        {
            whiteCells.remove(cell);
            greenCells.add(cell);
        }
        else if(greenCells.contains(cell))
        {
            greenCells.remove(cell);
            yellowCells.add(cell);
        }
    }

}
