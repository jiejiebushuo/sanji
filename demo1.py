import tkinter as tk
from tkinter import messagebox
#pip install mysql-connector-python
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
        self.create_widgets()
    def create_widgets(self):
        # 创建学生信息展示表格
        self.tree = ttk.Treeview(self.root, columns=("id", "name", "gender", "grade","class", "birthdate"), show="headings")
        self.tree.heading("id", text="学号")
        self.tree.heading("name", text="姓名")
        self.tree.heading("gender", text="性别")
        self.tree.heading("grade", text="年级")
        self.tree.heading("class", text="班级")
        self.tree.heading("birthdate", text="出生年月")
        self.tree.pack(fill=tk.BOTH, expand=True)
        self.load_students()
        # 新增按钮
        add_button = tk.Button(self.root, text="新增", command=self.add_student)
        add_button.pack(side=tk.BOTTOM)
    def load_students(self):
        # 从数据库加载学生信息
        cursor.execute("SELECT * FROM students")
        records = cursor.fetchall()
        for row in records:
            self.tree.insert("", tk.END, values=row)
    def add_student(self):
        # 弹出新增学生信息窗口
        self.new_window = tk.Toplevel(self.root)
        self.new_window.title("新增学生")
        # 输入框
        tk.Label(self.new_window, text="学号:").grid(row=0, column=0)
        self.student_id_entry = tk.Entry(self.new_window)
        self.student_id_entry.grid(row=0, column=1)
        tk.Label(self.new_window, text="姓名:").grid(row=1, column=0)
        self.name_entry = tk.Entry(self.new_window)
        self.name_entry.grid(row=1, column=1)
        tk.Label(self.new_window, text="性别:").grid(row=2, column=0)
        self.gender_var = tk.StringVar()
        tk.Radiobutton(self.new_window, text="男", variable=self.gender_var, value="男").grid(row=2, column=1)
        tk.Radiobutton(self.new_window, text="女", variable=self.gender_var, value="女").grid(row=2, column=2)
        tk.Label(self.new_window, text="年级:").grid(row=3, column=0)
        self.grade_var = tk.StringVar()
        grade_options = ["一年级", "二年级", "三年级", "四年级", "五年级", "六年级"]
        self.grade_menu = ttk.Combobox(self.new_window, textvariable=self.grade_var,
        values=grade_options)
        self.grade_menu.grid(row=3, column=1)
        tk.Label(self.new_window, text="班级:").grid(row=4, column=0)
        self.class_var = tk.StringVar()
        class_options = ["一班", "二班", "三班"]
        self.class_menu = ttk.Combobox(self.new_window, textvariable=self.class_var,
        values=class_options)
        self.class_menu.grid(row=4, column=1)
        tk.Label(self.new_window, text="出生年月:").grid(row=5, column=0)
        self.birthdate_entry = tk.Entry(self.new_window)
        self.birthdate_entry.grid(row=5, column=1)
        # 保存按钮
        save_button = tk.Button(self.new_window, text=" 保 存 ",
        command=self.save_student)
        save_button.grid(row=6, columnspan=2)
    def save_student(self):
        # 获取输入数据
        student_id = self.student_id_entry.get()
        name = self.name_entry.get()
        gender = self.gender_var.get()
        grade = self.grade_var.get()
        student_class = self.class_var.get()
        birthdate = self.birthdate_entry.get()
        # 保存到数据库
        try:
            cursor.execute("""
            INSERT INTO students (student_id, name, gender, grade, class, birthdate) VALUES (%s, %s, %s, %s, %s, %s)
            """, (student_id, name, gender, grade, student_class, birthdate))
            conn.commit()
            # 更新主界面
            self.tree.insert("", tk.END, values=(student_id, name, gender, grade,
            student_class, birthdate))
            self.new_window.destroy()
        except mysql.connector.Error as err:
            messagebox.showerror("错误", f"无法保存学生信息: {err}")
    def delete_student(self, student_id):
        try:
            cursor.execute("DELETE FROM students WHERE student_id = %s",
            (student_id,))
            conn.commit()
            # 在界面中移除
            selected_item = self.tree.selection()[0]
            self.tree.delete(selected_item)
        except mysql.connector.Error as err:
            messagebox.showerror("错误", f"无法删除学生信息: {err}")
# 运行程序
if __name__ == "__main__":
    root = tk.Tk()
    app = StudentManagementApp(root)
    root.mainloop()
# 程序结束后关闭数据库连接
conn.close()
