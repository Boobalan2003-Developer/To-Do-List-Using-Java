package to.pkgdo.list.application;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

class TaskCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ToDoListApp.Task) {
                ToDoListApp.Task task = (ToDoListApp.Task) value;
                setText(task.toString());
            }
            return c;
        }
    }
