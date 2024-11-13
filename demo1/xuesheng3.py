import tkinter as tk
from tkinter import messagebox
import mysql.connector
from tkinter import ttk

# 连接数据库
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="student_db"
)
cursor = conn.cursor()

# 创建表（如果尚未创建）
cursor.execute("""
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50),
    gender VARCHAR(10),
    grade VARCHAR(10),
    class VARCHAR(10),
    birthdate DATE
)
""")

class StudentManagementApp:
    def __init__(self, root):
        self.root = root
        self.root.title("学生信息管理系统")

        # 配置字段
        self.fields = {
            "student_id": {"label": "学号", "type": "entry"},
            "name": {"label": "姓名", "type": "entry"},
            "gender": {"label": "性别", "type": "radio", "options": ["男", "女"]},
            "grade": {"label": "年级", "type": "combo", "options": ["一年级", "二年级", "三年级", "四年级", "五年级", "六年级"]},
            "class": {"label": "班级", "type": "combo", "options": ["一班", "二班", "三班"]},
            "birthdate": {"label": "出生年月", "type": "entry"}
        }

        self.create_widgets()

    def create_widgets(self):
        # 创建学生信息展示表格
        self.tree = ttk.Treeview(self.root, columns=list(self.fields.keys()), show="headings")
        for field, info in self.fields.items():
            self.tree.heading(field, text=info["label"])
        self.tree.pack(fill=tk.BOTH, expand=True)
        
        self.load_students()

        # 在底部添加按钮
        button_frame = tk.Frame(self.root)
        button_frame.pack(side=tk.BOTTOM, fill=tk.X)

        add_button = tk.Button(button_frame, text="新增", command=self.add_student)
        add_button.pack(side=tk.LEFT, padx=10, pady=5)

        edit_button = tk.Button(button_frame, text="编辑", command=self.edit_selected_student)
        edit_button.pack(side=tk.LEFT, padx=10, pady=5)

        delete_button = tk.Button(button_frame, text="删除", command=self.delete_selected_student)
        delete_button.pack(side=tk.LEFT, padx=10, pady=5)

    def load_students(self):
        for item in self.tree.get_children():
            self.tree.delete(item)
        cursor.execute("SELECT * FROM students")
        records = cursor.fetchall()
        for row in records:
            self.tree.insert("", tk.END, values=row)

    def open_form(self, title, save_command, record=None):
        # 打开新增/编辑学生信息窗口
        form_window = tk.Toplevel(self.root)
        form_window.title(title)
        form_window.geometry("500x500")

        self.entries = {}
        
        for i, (field, info) in enumerate(self.fields.items()):
            tk.Label(form_window, text=info["label"]).grid(row=i, column=0)

            if info["type"] == "entry":
                entry = tk.Entry(form_window)
                entry.grid(row=i, column=1)
                if record:
                    entry.insert(0, record[i])
                self.entries[field] = entry

            elif info["type"] == "radio":
                var = tk.StringVar(value=record[i] if record else info["options"][0])
                self.entries[field] = var
                for j, option in enumerate(info["options"]):
                    tk.Radiobutton(form_window, text=option, variable=var, value=option).grid(row=i, column=1 + j)

            elif info["type"] == "combo":
                combo = ttk.Combobox(form_window, values=info["options"])
                combo.grid(row=i, column=1)
                if record:
                    combo.set(record[i])
                self.entries[field] = combo

        save_button = tk.Button(form_window, text="保存", command=lambda: save_command(form_window))
        save_button.grid(row=len(self.fields), columnspan=2)

    def add_student(self):
        self.open_form("新增学生", self.save_new_student)

    def save_new_student(self, window):
        data = {field: entry.get() if isinstance(entry, (tk.Entry, ttk.Combobox)) else entry.get() for field, entry in self.entries.items()}
        try:
            cursor.execute("""
                INSERT INTO students (student_id, name, gender, grade, class, birthdate) 
                VALUES (%(student_id)s, %(name)s, %(gender)s, %(grade)s, %(class)s, %(birthdate)s)
            """, data)
            conn.commit()
            self.load_students()
            window.destroy()
        except mysql.connector.Error as err:
            messagebox.showerror("错误", f"无法保存学生信息: {err}")

    def edit_selected_student(self):
        selected = self.tree.selection()
        if not selected:
            messagebox.showwarning("警告", "请先选择一个学生进行编辑")
            return

        item = self.tree.item(selected[0])
        student_id = item["values"][0]
        cursor.execute("SELECT * FROM students WHERE student_id = %s", (student_id,))
        record = cursor.fetchone()

        self.open_form("编辑学生信息", lambda window: self.save_edited_student(window, student_id), record)

    def save_edited_student(self, window, student_id):
        data = {field: entry.get() if isinstance(entry, (tk.Entry, ttk.Combobox)) else entry.get() for field, entry in self.entries.items()}
        data["student_id"] = student_id  # 保持当前学生ID不变
        try:
            cursor.execute("""
                UPDATE students SET name=%(name)s, gender=%(gender)s, grade=%(grade)s, class=%(class)s, birthdate=%(birthdate)s 
                WHERE student_id=%(student_id)s
            """, data)
            conn.commit()
            self.load_students()
            window.destroy()
        except mysql.connector.Error as err:
            messagebox.showerror("错误", f"无法保存学生信息: {err}")

    def delete_selected_student(self):
        selected = self.tree.selection()
        if not selected:
            messagebox.showwarning("警告", "请先选择一个学生进行删除")
            return

        item = self.tree.item(selected[0])
        student_id = item["values"][0]

        try:
            cursor.execute("DELETE FROM students WHERE student_id = %s", (student_id,))
            conn.commit()
            self.load_students()
        except mysql.connector.Error as err:
            messagebox.showerror("错误", f"无法删除学生信息: {err}")
    
# 运行程序
if __name__ == "__main__":
    root = tk.Tk()
    app = StudentManagementApp(root)
    root.mainloop()

# 程序结束后关闭数据库连接
conn.close()
