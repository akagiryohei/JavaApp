package View;

import javax.swing.SwingUtilities;
import Interface.View.IUIDispatcher;

public class SwingUIDispatcher implements IUIDispatcher
{
  @Override
  public void dispatch(Runnable task)
  {
    SwingUtilities.invokeLater(task);
  }
}
